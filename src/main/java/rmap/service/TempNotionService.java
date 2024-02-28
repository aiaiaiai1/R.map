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
public class TempNotionService {

    private final NotionRepository notionRepository;
    private final EdgeRepository edgeRepository;
    private final NotionFolderRepository notionFolderRepository;

    public Notion readNotion(Long notionId) {
        return notionRepository.findByIdOrThrow(notionId);
    }

    @Transactional
    public NotionIdResponse buildNotion(BuildNotionRequest request) {
        if (isInitialNotionRequest(request)) {
            Notion notion = createInitialNotion(request);
            return new NotionIdResponse(notion.getId());
        }

        Notion notion = createNotionConnectedWithRelatedNotion(request);
        return new NotionIdResponse(notion.getId());
    }

    private boolean isInitialNotionRequest(BuildNotionRequest request) {
        return request.getRelatedNotion() == null;
    }

    private Notion createInitialNotion(BuildNotionRequest request) {
        NotionFolder notionFolder = notionFolderRepository.findByIdOrThrow(request.getNotionFolderId());
        Notion notion = new Notion(request.getName(), request.getContent(), notionFolder);
        return notionRepository.save(notion);
    }

    private Notion createNotionConnectedWithRelatedNotion(BuildNotionRequest request) {
        NotionFolder notionFolder = notionFolderRepository.findByIdOrThrow(request.getNotionFolderId());
        Notion relatedNotion = notionRepository.findByIdOrThrow(request.getRelatedNotion().getId());

        Notion notion = new Notion(request.getName(), request.getContent(), notionFolder);
        Notion savedNotion = notionRepository.save(notion);
        Edge edge1 = savedNotion.tempConnect(relatedNotion, request.getRelatedNotion().getRelevance());
        Edge edge2 = relatedNotion.tempConnect(savedNotion, request.getRelatedNotion().getReverseRelevance());
        edgeRepository.save(edge1);
        edgeRepository.save(edge2);
        return savedNotion;
    }

    @Transactional
    public void demolishNotion(Long notionId) {
        Notion notion = notionRepository.findByIdOrThrow(notionId);
        List<Edge> edges = edgeRepository.findAllByNotionId(notion.getId());
        edgeRepository.deleteAllInBatch(edges);
        notionRepository.delete(notion);
    }

    @Transactional
    public void editNotion(Long notionId, String name, String content) {
        Notion notion = notionRepository.findByIdOrThrow(notionId);
        notion.editName(name);
        notion.editContent(content);
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
                notion.tempEditDescription(relatedNotion, request.getRelevance());
                relatedNotion.tempEditDescription(notion, request.getReverseRelevance());
            }

            if (connectionRequestIds.contains(request.getId())) {
                Edge edge1 = notion.tempConnect(relatedNotion, request.getRelevance());
                Edge edge2 = relatedNotion.tempConnect(notion, request.getReverseRelevance());
                edgeRepository.save(edge1);
                edgeRepository.save(edge2);
            }
        }
    }

    private void disconnectAll(Notion notion, List<Long> disconnectionRequestIds) {
        for (Long notionId : disconnectionRequestIds) {
            Notion relatedNotion = notionRepository.findByIdOrThrow(notionId);
            notion.tempDisconnect(relatedNotion);
            relatedNotion.tempDisconnect(notion);
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

    @Transactional
    public void disconnectNotionRelation(Long notionAId, Long notionBId) {
        Notion notionA = notionRepository.findByIdOrThrow(notionAId);
        Notion notionB = notionRepository.findByIdOrThrow(notionBId);

        notionA.tempDisconnect(notionB);
        notionB.tempDisconnect(notionA);
    }

}
