package rmap.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rmap.entity.Notion;
import rmap.entity.NotionFolder;
import rmap.repository.NotionRepository;

@Service
@RequiredArgsConstructor
public class NotionService {

    private final NotionRepository notionRepository;

    public Notion createNotion(String name, String content, NotionFolder notionFolder) {
        Notion notion = new Notion(name, content, notionFolder);
        return notionRepository.save(notion);
    }

    public Notion readNotion(Long notionId) {
        return notionRepository.findByIdOrThrow(notionId);
    }

    public void deleteNotion(Notion notion) {
        notionRepository.delete(notion);
    }

    @Transactional
    public void editNotion(Long notionId, String name, String content) {
        Notion notion = readNotion(notionId);
        notion.editName(name);
        notion.editContent(content);
    }

}
