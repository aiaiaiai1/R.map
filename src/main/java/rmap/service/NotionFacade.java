package rmap.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rmap.entity.Notion;
import rmap.request.BuildNotionRequest;
import rmap.response.NotionIdResponse;
import rmap.response.NotionResponse;

@Service
@RequiredArgsConstructor
public class NotionFacade {

    private final NotionService notionService;
    private final EdgeService edgeService;

    @Transactional
    public NotionIdResponse buildNotion(BuildNotionRequest request) {
        Notion notion = notionService.createNotion(request.getName(), request.getContent());
        if (request.getRelatedNotion().getId() == null) {
            return new NotionIdResponse(notion.getId());
        }

        Notion relatedNotion = notionService.readNotion(request.getRelatedNotion().getId());
        edgeService.connect(notion, relatedNotion, "");
        edgeService.connect(relatedNotion, notion, "");
        return new NotionIdResponse(notion.getId());
    }

    public NotionResponse readNotion(Long notionId) {
        Notion notion = notionService.readNotion(notionId);
        return NotionResponse.from(notion);
    }

}
