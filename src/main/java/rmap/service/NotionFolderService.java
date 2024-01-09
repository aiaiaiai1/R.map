package rmap.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import rmap.entity.NotionFolder;
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

    public NotionFolder deleteNotionFolder(Long notionFolderId) {
        NotionFolder notionFolder = notionFolderRepository.findById(notionFolderId)
                .orElseThrow(() -> new IllegalArgumentException("노션 폴더가 존재하지 않습니다."));
        notionFolderRepository.delete(notionFolder);
        return notionFolder;
    }

}
