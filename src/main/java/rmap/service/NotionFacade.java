package rmap.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rmap.entity.Graph;
import rmap.entity.Notion;
import rmap.request.BuildNotionRequest;
import rmap.response.NotionIdResponse;
import rmap.response.NotionResponse;

@Service
@RequiredArgsConstructor
public class NotionFacade {

    private final NotionService notionService;
    private final EdgeService edgeService;
    private final GraphService graphService;

    @Transactional
    public NotionIdResponse buildNotion(BuildNotionRequest request) {
        if (request.getRelatedNotion().getId() == null) {
            Graph graph = graphService.createGraph(request.getName());
            Notion notion = notionService.createNotion(request.getName(), request.getContent(), graph);
            return new NotionIdResponse(notion.getId());
        }

        Notion relatedNotion = notionService.readNotion(request.getRelatedNotion().getId());
        Notion notion = notionService.createNotion(request.getName(), request.getContent(), relatedNotion.getGraph());
        edgeService.connect(notion, relatedNotion, "");
        edgeService.connect(relatedNotion, notion, "");
        return new NotionIdResponse(notion.getId());
    }

    public NotionResponse readNotion(Long notionId) {
        Notion notion = notionService.readNotion(notionId);
        return NotionResponse.from(notion);
    }

}
