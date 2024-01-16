package rmap.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rmap.entity.NotionFolder;
import rmap.exception.EntityNotFoundException;
import rmap.exception.type.NotionFolderExceptionType;

@Repository
public interface NotionFolderRepository extends JpaRepository<NotionFolder, Long> {

    default NotionFolder findByIdOrThrow(Long notionFolderId) {
        NotionFolder notionFolder = findById(notionFolderId)
                .orElseThrow(() -> new EntityNotFoundException(NotionFolderExceptionType.NOT_FOUND));
        return notionFolder;
    }

}
