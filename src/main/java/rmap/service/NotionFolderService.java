package rmap.service;

import java.util.Comparator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

    @Transactional
    public void mergeNotionFolder(Long notionFolderId, List<Long> notionFolderIds) {
        NotionFolder notionFolder = notionFolderRepository.findByIdOrThrow(notionFolderId);
        for (Long id : notionFolderIds) {
            List<Notion> notions = notionRepository.findAllInNotionFolder(id);
            notions.stream()
                    .forEach(n -> n.changeNotionFolder(notionFolder));
            notionFolderRepository.deleteById(id);
        }
    }

    @Transactional
    public void mergeNotionFolderWithNew(String notionFolderName, List<Long> notionFolderIds) {
        NotionFolder notionFolder = createNotionFolder(notionFolderName);
        mergeNotionFolder(notionFolder.getId(), notionFolderIds);
    }

    public List<List<Notion>> findAllGraph(Long notionFolderId) {
        NotionFolder notionFolder = notionFolderRepository.findByIdOrThrow(notionFolderId);
        List<Notion> notions = notionRepository.findAllInNotionFolder(notionFolder.getId());
        return NotionSearcher.getGraphs(notions);
    }

}
