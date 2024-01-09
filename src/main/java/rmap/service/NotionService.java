package rmap.service;

import java.util.Comparator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rmap.entity.Graph;
import rmap.entity.Notion;
import rmap.entity.NotionFolder;
import rmap.repository.NotionFolderRepository;
import rmap.repository.NotionRepository;

@Service
@RequiredArgsConstructor
public class NotionService {

    private final NotionRepository notionRepository;
    private final NotionFolderRepository notionFolderRepository;

    public Notion createNotion(String name, String content, Graph graph) {
        Notion notion = new Notion(name, content, graph);
        return notionRepository.save(notion);
    }

    public Notion readNotion(Long notionId) {
        Notion notion = notionRepository.findById(notionId)
                .orElseThrow(() -> new IllegalArgumentException("해당 노션이 존재하지 않습니다."));
        return notion;
    }

    public List<Notion> readAllInNotionFolder(Long notionFolderId) {
        NotionFolder notionFolder = notionFolderRepository.findById(notionFolderId)
                .orElseThrow(() -> new IllegalArgumentException("해당 노션 폴더가 존재하지 않습니다."));
        List<Notion> notions = notionRepository.findAllInNotionFolder(notionFolder.getId());
        return notions.stream()
                .sorted(Comparator.comparing(Notion::getName))
                .toList();
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
