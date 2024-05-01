package rmap.service;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rmap.entity.Edge;
import rmap.entity.Notion;
import rmap.entity.NotionFolder;
import rmap.entity.User;
import rmap.exception.EtcException;
import rmap.exception.type.EtcExceptionType;
import rmap.repository.EdgeRepository;
import rmap.repository.NotionFolderRepository;
import rmap.repository.NotionRepository;
import rmap.request.BuildNotionRequest;
import rmap.request.PatchRelatedNotionRequest;
import rmap.response.NotionIdResponse;

@Service
@RequiredArgsConstructor
public class NotionRelationService {

    private final NotionRepository notionRepository;
    private final EdgeRepository edgeRepository;
    private final NotionFolderRepository notionFolderRepository;


    @Transactional
    public void disconnectNotionRelation(User loginedUser, Long notionAId, Long notionBId) {
        Notion notionA = notionRepository.findByIdOrThrow(notionAId);
        Notion notionB = notionRepository.findByIdOrThrow(notionBId);

        Validator.validateNotionOwner(notionA, loginedUser);
        Validator.validateNotionOwner(notionB, loginedUser);

        notionA.disconnect(notionB);
        notionB.disconnect(notionA);
    }

    @Transactional
    public NotionIdResponse createNotionConnectedWithRelatedNotion(User loginedUser, BuildNotionRequest request) {
        NotionFolder notionFolder = notionFolderRepository.findByIdOrThrow(request.getNotionFolderId());
        Validator.validateNotionFolderOwner(notionFolder, loginedUser);

        Notion relatedNotion = notionRepository.findByIdOrThrow(request.getRelatedNotion().getId());
        Validator.validateNotionOwner(relatedNotion, loginedUser);

        Notion notion = new Notion(request.getName(), request.getContent(), notionFolder);
        Notion savedNotion = notionRepository.save(notion);

        Edge edge1 = savedNotion.connect(relatedNotion, request.getRelatedNotion().getRelevance());
        Edge edge2 = relatedNotion.connect(savedNotion, request.getRelatedNotion().getReverseRelevance());
        edgeRepository.save(edge1);
        edgeRepository.save(edge2);
        return new NotionIdResponse(savedNotion.getId());
    }

    @Transactional
    public void editNotionRelations(User loginedUser, Long targetNotionId, List<PatchRelatedNotionRequest> requests) {
        Notion notion = notionRepository.findByIdOrThrow(targetNotionId);
        Validator.validateNotionOwner(notion, loginedUser);

        List<Long> relatedNotionIds = getRelatedNotionIds(notion);
        List<Long> requestIds = getRequestIds(requests);

        validateDuplicateIds(requestIds);

        List<Long> connectionRequestIds = subtractIds(requestIds, relatedNotionIds);
        List<Long> editingRequestIds = filterCommonIds(relatedNotionIds, requestIds);
        List<Long> disconnectionRequestIds = subtractIds(relatedNotionIds, requestIds);

        connectAndEditAll(loginedUser, notion, requests, connectionRequestIds, editingRequestIds);
        disconnectAll(loginedUser, notion, disconnectionRequestIds);
    }

    private List<Long> getRequestIds(List<PatchRelatedNotionRequest> requests) {
        return requests.stream()
                .map(PatchRelatedNotionRequest::getId)
                .toList();
    }

    private List<Long> getRelatedNotionIds(Notion notion) {
        return notion.getEdges().stream()
                .map(Edge::getTargetNotion)
                .map(Notion::getId)
                .toList();
    }

    private void connectAndEditAll(
            User loginedUser,
            Notion notion,
            List<PatchRelatedNotionRequest> requests,
            List<Long> connectionRequestIds,
            List<Long> editingRequestIds
    ) {
        for (PatchRelatedNotionRequest request : requests) {
            Notion relatedNotion = notionRepository.findByIdOrThrow(request.getId());
            Validator.validateNotionOwner(relatedNotion, loginedUser);
            if (editingRequestIds.contains(request.getId())) {
                notion.editDescription(relatedNotion, request.getRelevance());
                relatedNotion.editDescription(notion, request.getReverseRelevance());
            }

            if (connectionRequestIds.contains(request.getId())) {
                Edge edge1 = notion.connect(relatedNotion, request.getRelevance());
                Edge edge2 = relatedNotion.connect(notion, request.getReverseRelevance());
                edgeRepository.save(edge1);
                edgeRepository.save(edge2);
            }
        }
    }

    private void disconnectAll(User loginedUser, Notion notion, List<Long> disconnectionRequestIds) {
        for (Long notionId : disconnectionRequestIds) {
            Notion relatedNotion = notionRepository.findByIdOrThrow(notionId);
            Validator.validateNotionOwner(relatedNotion, loginedUser);
            notion.disconnect(relatedNotion);
            relatedNotion.disconnect(notion);
        }
    }

    private List<Long> filterCommonIds(List<Long> ids, List<Long> filterIds) {
        List<Long> commonIds = new ArrayList<>(ids);
        commonIds.retainAll(filterIds);
        return commonIds;
    }

    private List<Long> subtractIds(List<Long> originalIds, List<Long> subtrahendIds) {
        List<Long> subtractedIds = new ArrayList<>(originalIds);
        subtractedIds.removeAll(subtrahendIds);
        return subtractedIds;
    }

    private void validateDuplicateIds(List<Long> ids) {
        if (ids.stream().distinct().count() != ids.size()) {
            throw new EtcException(EtcExceptionType.DUPLICATE_SIZE);
        }
    }

}
