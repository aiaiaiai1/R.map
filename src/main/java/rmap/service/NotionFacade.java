package rmap.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rmap.entity.Edge;
import rmap.entity.Notion;
import rmap.entity.NotionFolder;
import rmap.request.BuildNotionRequest;
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

//    @Transactional
//    public void editNotionRelations(Long notionId, List<PatchRelatedNotionRequest> requests) {
//        Notion notion = notionService.readNotion(notionId);
//        NotionFolder notionFolder = notion.getGraph().getNotionFolder();
//
//        // 2
//        List<Long> ids = requests.stream()
//                .map(request -> request.getId())
//                .toList();
//        if (ids.stream().distinct().count() != ids.size()) {
//            throw new InvalidAcessException(InvalidAcessExceptionType.DUPLICATE_IDS);
//        }
//
//        // 3
//        List<Edge> edges = edgeService.findAllByNotionId(notion.getId());
//        edgeService.deleteEdges(edges);
//
//        // 4
//        for (PatchRelatedNotionRequest request : requests) {
//            Notion relatedNotion = notionService.readNotion(request.getId());
//            // 5
//            if (!notionFolder.contains(relatedNotion.getGraph())) {
//                throw new InvalidAcessException(InvalidAcessExceptionType.NOT_MATCH_NOTIONFOLDER_AND_NOTION);
//            }
//
//            // 6
//            edgeService.connect(notion, relatedNotion, request.getRelevance());
//            edgeService.connect(relatedNotion, notion, request.getReverseRelevance());
//
//            // 7
//            Graph targetGraph = notion.getGraph();
//            Graph sourceGraph = relatedNotion.getGraph();
//
//            if (!targetGraph.equals(sourceGraph)) {
//                relatedNotion.changeGraph(targetGraph);
//                List<Notion> notions = graphService.readNotionsOfGraph(sourceGraph.getId());
//                notions.stream()
//                        .forEach(n -> n.changeGraph(targetGraph));
//            }
//        }
//
//    }

//    @Transactional
//    public void disconnectNotionRelation(Long notionAId, Long notionBId) {
//        Notion notionA = notionService.readNotion(notionAId);
//        Notion notionB = notionService.readNotion(notionBId);
//        edgeService.disconnect(notionA, notionB);
//        edgeService.disconnect(notionB, notionA);
//
//        List<Notion> notionsA = NotionSearcher.searchDepthFirst(notionA);
//
//        if (isEmpty(notionsA, notionB)) {
//            NotionFolder notionFolder = notionB.getGraph().getNotionFolder();
//            Graph newGraph = graphService.createGraph(notionFolder);
//
//            List<Notion> notionsB = NotionSearcher.searchDepthFirst(notionB);
//            migrateNotions(newGraph, notionsB);
//        }
//    }
//
//    private boolean isEmpty(List<Notion> notions, Notion notion) {
//        return !notions.contains(notion);
//    }
//
//    private void migrateNotions(Graph graph, List<Notion> notions) {
//        for (Notion notion : notions) {
//            notion.changeGraph(graph);
//        }
//    }

}
