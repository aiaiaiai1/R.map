package rmap.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rmap.entity.Edge;
import rmap.entity.Graph;
import rmap.entity.Notion;
import rmap.entity.NotionFolder;
import rmap.exception.InvalidAcessException;
import rmap.exception.type.InvalidAcessExceptionType;
import rmap.request.BuildNotionRequest;
import rmap.request.PatchRelatedNotionRequest;
import rmap.response.NotionIdResponse;
import rmap.response.NotionResponse;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotionFacade {

    private final NotionService notionService;
    private final NotionFolderService notionFolderService;
    private final EdgeService edgeService;
    private final GraphService graphService;

    @Transactional
    public NotionIdResponse buildNotion(BuildNotionRequest request) {
        if (isInitial(request)) {
            Notion notion = createInitialNotion(request);
            return new NotionIdResponse(notion.getId());
        }

        Notion notion = createNotionConnectedWithRelatedNotion(request);
        return new NotionIdResponse(notion.getId());
    }

    private boolean isInitial(BuildNotionRequest request) {
        return request.getRelatedNotion() == null;
    }


    private Notion createInitialNotion(BuildNotionRequest request) {
        NotionFolder notionFolder = notionFolderService.readNotionFolder(request.getNotionFolderId());
        Graph graph = graphService.createGraph(notionFolder);
        return notionService.createNotion(request.getName(), request.getContent(), graph);
    }

    private Notion createNotionConnectedWithRelatedNotion(BuildNotionRequest request) {
        NotionFolder notionFolder = notionFolderService.readNotionFolder(request.getNotionFolderId());
        Notion relatedNotion = notionService.readNotion(request.getRelatedNotion().getId());
        Graph graph = relatedNotion.getGraph();
        if (!notionFolder.contains(graph)) {
            throw new InvalidAcessException(InvalidAcessExceptionType.NOT_MATCH_NOTIONFOLDER_AND_NOTION);
        }
        Notion notion = notionService.createNotion(request.getName(), request.getContent(), graph);
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
        NotionFolder notionFolder = notion.getGraph().getNotionFolder();

        // 2
        List<Long> ids = requests.stream()
                .map(request -> request.getId())
                .toList();
        if (ids.stream().distinct().count() != ids.size()) {
            throw new InvalidAcessException(InvalidAcessExceptionType.DUPLICATE_IDS);
        }

        // 3
        List<Edge> edges = edgeService.findAllByNotionId(notion.getId());
        edgeService.deleteEdges(edges);

        // 4
        for (PatchRelatedNotionRequest request : requests) {
            Notion relatedNotion = notionService.readNotion(request.getId());
            // 5
            if (!notionFolder.contains(relatedNotion.getGraph())) {
                throw new InvalidAcessException(InvalidAcessExceptionType.NOT_MATCH_NOTIONFOLDER_AND_NOTION);
            }

            // 6
            edgeService.connect(notion, relatedNotion, request.getRelevance());
            edgeService.connect(relatedNotion, notion, request.getReverseRelevance());

            // 7
            Graph targetGraph = notion.getGraph();
            Graph sourceGraph = relatedNotion.getGraph();
            
            if (!targetGraph.equals(sourceGraph)) {
                relatedNotion.changeGraph(targetGraph);
                List<Notion> notions = graphService.readNotionsOfGraph(sourceGraph.getId());
                notions.stream()
                        .forEach(n -> n.changeGraph(targetGraph));
            }
        }

    }

}
