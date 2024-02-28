package rmap.service;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rmap.entity.Edge;
import rmap.entity.Notion;
import rmap.entity.NotionFolder;
import rmap.exception.InvalidAcessException;
import rmap.exception.type.InvalidAcessExceptionType;
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
    public void disconnectNotionRelation(Long notionAId, Long notionBId) {
        Notion notionA = notionRepository.findByIdOrThrow(notionAId);
        Notion notionB = notionRepository.findByIdOrThrow(notionBId);

        notionA.disconnect(notionB);
        notionB.disconnect(notionA);
    }

    @Transactional
    public NotionIdResponse createNotionConnectedWithRelatedNotion(BuildNotionRequest request) {
        NotionFolder notionFolder = notionFolderRepository.findByIdOrThrow(request.getNotionFolderId());
        Notion relatedNotion = notionRepository.findByIdOrThrow(request.getRelatedNotion().getId());

        Notion notion = new Notion(request.getName(), request.getContent(), notionFolder);
        Notion savedNotion = notionRepository.save(notion);

        Edge edge1 = savedNotion.connect(relatedNotion, request.getRelatedNotion().getRelevance());
        Edge edge2 = relatedNotion.connect(savedNotion, request.getRelatedNotion().getReverseRelevance());
        edgeRepository.save(edge1);
        edgeRepository.save(edge2);
        return new NotionIdResponse(savedNotion.getId());
    }

    @Transactional
    public void editNotionRelations(Long notionId, List<PatchRelatedNotionRequest> requests) {
        Notion notion = notionRepository.findByIdOrThrow(notionId);

        List<Long> relatedNotionIds = getRelatedNotionIds(notion);
        List<Long> requestIds = getRequestIds(requests);

        validateDuplicateIds(requestIds);

        List<Long> connectionRequestIds = subtractIds(requestIds, relatedNotionIds);
        List<Long> editingRequestIds = filterCommonIds(relatedNotionIds, requestIds);
        List<Long> disconnectionRequestIds = subtractIds(relatedNotionIds, requestIds);

        connectAndEditAll(notion, requests, connectionRequestIds, editingRequestIds);
        disconnectAll(notion, disconnectionRequestIds);
    }

    private boolean isInitialNotionRequest(BuildNotionRequest request) {
        return request.getRelatedNotion() == null;
    }

    private Notion createInitialNotion(BuildNotionRequest request) {
        NotionFolder notionFolder = notionFolderRepository.findByIdOrThrow(request.getNotionFolderId());
        Notion notion = new Notion(request.getName(), request.getContent(), notionFolder);
        return notionRepository.save(notion);
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
            Notion notion,
            List<PatchRelatedNotionRequest> requests,
            List<Long> connectionRequestIds,
            List<Long> editingRequestIds
    ) {
        for (PatchRelatedNotionRequest request : requests) {
            Notion relatedNotion = notionRepository.findByIdOrThrow(request.getId());
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

    private void disconnectAll(Notion notion, List<Long> disconnectionRequestIds) {
        for (Long notionId : disconnectionRequestIds) {
            Notion relatedNotion = notionRepository.findByIdOrThrow(notionId);
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
            throw new InvalidAcessException(InvalidAcessExceptionType.DUPLICATE_IDS);
        }
    }

}
