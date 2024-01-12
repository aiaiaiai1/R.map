package rmap.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rmap.entity.Edge;
import rmap.entity.Graph;
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
    private final GraphService graphService;

    @Transactional
    public NotionIdResponse buildNotion(BuildNotionRequest request) {
        NotionFolder notionFolder = notionFolderService.readNotionFolder(request.getNotionFolderId());

        if (request.getRelatedNotion().getId() == null) {
            Graph graph = graphService.createGraph(notionFolder);
            Notion notion = notionService.createNotion(request.getName(), request.getContent(), graph);
            return new NotionIdResponse(notion.getId());
        }

        Notion relatedNotion = notionService.readNotion(request.getRelatedNotion().getId());
        Graph graph = relatedNotion.getGraph();
        if (!notionFolder.contains(graph)) {
            throw new IllegalArgumentException("잘못된 접근 입니다.(해당 노션 폴더에 소스 노션이 존재 하지 않습니다.)");
        }

        Notion notion = notionService.createNotion(request.getName(), request.getContent(), graph);
        edgeService.connect(notion, relatedNotion, "");
        edgeService.connect(relatedNotion, notion, "");
        return new NotionIdResponse(notion.getId());
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
