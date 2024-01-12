package rmap.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import rmap.entity.NotionFolder;
import rmap.exception.NotFoundException;
import rmap.exception.type.NotionFolderExceptionType;
import rmap.repository.NotionFolderRepository;

@Service
@RequiredArgsConstructor
public class NotionFolderService {

    private final NotionFolderRepository notionFolderRepository;

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

}
