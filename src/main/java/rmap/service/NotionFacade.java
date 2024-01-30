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
        edgeService.connect(relatedNotion, notion, "");
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

}
