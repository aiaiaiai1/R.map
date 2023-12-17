package rmap.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import rmap.entity.Graph;
import rmap.entity.Notion;
import rmap.repository.NotionRepository;

@Service
@RequiredArgsConstructor
public class NotionService {

    private final NotionRepository notionRepository;

    public Notion createNotion(String name, String content, Graph graph) {
        Notion notion = new Notion(name, content, graph);
        return notionRepository.save(notion);
    }

    public Notion readNotion(Long notionId) {
        Notion notion = notionRepository.findById(notionId)
                .orElseThrow(() -> new IllegalArgumentException("해당 노션이 존재하지 않습니다."));
        return notion;
    }

}
