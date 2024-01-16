package rmap.service;

import java.util.Comparator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import rmap.entity.Notion;
import rmap.entity.NotionFolder;
import rmap.repository.NotionFolderRepository;
import rmap.repository.NotionRepository;
import rmap.response.NotionFolderResponse;

@Service
@RequiredArgsConstructor
public class NotionFolderService {

    private final NotionFolderRepository notionFolderRepository;
    private final NotionRepository notionRepository;

    public List<NotionFolder> readAll() {
        return notionFolderRepository.findAll();
    }

    public NotionFolder createNotionFolder(String name) {
        NotionFolder notionFolder = new NotionFolder(name);
        return notionFolderRepository.save(notionFolder);
    }

    public void deleteNotionFolder(Long notionFolderId) {
        NotionFolder notionFolder = notionFolderRepository.findByIdOrThrow(notionFolderId);
        notionFolderRepository.delete(notionFolder);
    }

    public NotionFolder readNotionFolder(Long notionFolderId) {
        return notionFolderRepository.findByIdOrThrow(notionFolderId);
    }

    public NotionFolderResponse readNotionFolderInfo(Long notionFolderId) {
        NotionFolder notionFolder = readNotionFolder(notionFolderId);
        List<Notion> notions = notionRepository.findAllInNotionFolder(notionFolder.getId());
        List<Notion> sortedNotions = notions.stream()
                .sorted(Comparator.comparing(Notion::getName))
                .toList();
        return NotionFolderResponse.of(notionFolder, sortedNotions);
    }

}
