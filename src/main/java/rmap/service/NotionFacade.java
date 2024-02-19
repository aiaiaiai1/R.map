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
import rmap.request.BuildNotionRequest;
import rmap.request.PatchRelatedNotionRequest;
import rmap.response.NotionIdResponse;
import rmap.response.NotionResponse;

@Service
@RequiredArgsConstructor
public class NotionFacade {

    private final NotionService notionService;
    private final NotionFolderService notionFolderService;
    private final EdgeService edgeService;

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
        NotionFolder notionFolder = notionFolderService.readNotionFolder(request.getNotionFolderId());
        return notionService.createNotion(request.getName(), request.getContent(), notionFolder);
    }

    private Notion createNotionConnectedWithRelatedNotion(BuildNotionRequest request) {
        NotionFolder notionFolder = notionFolderService.readNotionFolder(request.getNotionFolderId());
        Notion relatedNotion = notionService.readNotion(request.getRelatedNotion().getId());
        Notion notion = notionService.createNotion(request.getName(), request.getContent(), notionFolder);

        edgeService.connect(notion, relatedNotion, request.getRelatedNotion().getRelevance());
        edgeService.connect(relatedNotion, notion, request.getRelatedNotion().getReverseRelevance());
        return notion;
    }

    public NotionResponse readNotion(Long notionId) {
        Notion notion = notionService.readNotion(notionId);
        return NotionResponse.from(notion);
    }

    @Transactional
    public void demolishNotion(Long notionId) {
        Notion notion = notionService.readNotion(notionId);
        List<Edge> edges = edgeService.findAllByNotionId(notion.getId());
        edgeService.deleteEdges(edges);
        notionService.deleteNotion(notion);
    }

    @Transactional
    public void editNotion(Long notionId, String name, String content) {
        notionService.editNotion(notionId, name, content);
    }

    @Transactional
    public void editNotionRelations(Long notionId, List<PatchRelatedNotionRequest> requests) {
        Notion notion = notionService.readNotion(notionId);

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
            Notion relatedNotion = notionService.readNotion(request.getId());
            if (editingRequestIds.contains(request.getId())) {
                edgeService.editDescription(notion, relatedNotion, request.getRelevance());
                edgeService.editDescription(relatedNotion, notion, request.getReverseRelevance());
            }

            if (connectionRequestIds.contains(request.getId())) {
                edgeService.connect(notion, relatedNotion, request.getRelevance());
                edgeService.connect(relatedNotion, notion, request.getReverseRelevance());
            }
        }
    }

    private void disconnectAll(Notion notion, List<Long> disconnectionRequestIds) {
        for (Long notionId : disconnectionRequestIds) {
            Notion relatedNotion = notionService.readNotion(notionId);
            edgeService.disconnect(notion, relatedNotion);
            edgeService.disconnect(relatedNotion, notion);
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
        Notion notionA = notionService.readNotion(notionAId);
        Notion notionB = notionService.readNotion(notionBId);

        edgeService.disconnect(notionA, notionB);
        edgeService.disconnect(notionB, notionA);
    }
}

