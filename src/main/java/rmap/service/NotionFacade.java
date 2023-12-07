package rmap.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rmap.entity.Notion;
import rmap.request.BuildNotionRequest;
import rmap.response.NotionResponse;
import rmap.service.EdgeService;
import rmap.service.NotionService;

@Service
@RequiredArgsConstructor
public class NotionFacade {

    private final NotionService notionService;
    private final EdgeService edgeService;

    @Transactional
    public Long buildNotion(BuildNotionRequest request) {
        Notion notion = notionService.createNotion(request.getName(), request.getContent());
        if (request.getRelatedNotion().getId() == null) {
            return notion.getId();
        }

        Notion relatedNotion = notionService.readNotion(request.getRelatedNotion().getId());
        edgeService.connect(notion, relatedNotion, null);
        edgeService.connect(relatedNotion, notion, null);
        return notion.getId();
    }

    public NotionResponse readNotion(Long notionId) {
        Notion notion = notionService.readNotion(notionId);
        return NotionResponse.from(notion);
    }

}
