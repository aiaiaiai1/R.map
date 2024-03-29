package rmap.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import rmap.entity.Notion;
import rmap.exception.EntityNotFoundException;
import rmap.exception.type.NotionExceptionType;

public interface NotionRepository extends JpaRepository<Notion, Long> {

    default Notion findByIdOrThrow(Long notionId) {
        Notion notion = findById(notionId)
                .orElseThrow(() -> new EntityNotFoundException(NotionExceptionType.NOT_FOUND));
        return notion;
    }

    @Query(value = "select n.id, n.notion_folder_id, n.name, n.content "
            + "from notion_folder as nf "
            + "join notion as n on nf.id = n.notion_folder_id "
            + "where nf.id = :notionFolderId", nativeQuery = true)
    List<Notion> findAllInNotionFolder(Long notionFolderId);
}
