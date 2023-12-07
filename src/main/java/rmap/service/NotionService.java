package rmap.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import rmap.entity.Notion;
import rmap.repository.NotionRepository;

@Service
@RequiredArgsConstructor
public class NotionService {

    private final NotionRepository notionRepository;

    public Notion createNotion(String name, String content) {
        Notion notion = new Notion(name, content);
        return notionRepository.save(notion);
    }

    public Notion readNotion(Long notionId) {
        Notion notion = notionRepository.findById(notionId)
                .orElseThrow(() -> new IllegalArgumentException());
        return notion;
    }

}
