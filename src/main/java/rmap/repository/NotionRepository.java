package rmap.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import rmap.entity.Notion;

public interface NotionRepository extends JpaRepository<Notion, Long> {

    @Query(value = "select * from notion_folder as nf "
            + "join graph as g on nf.id = g.notion_folder_id "
            + "join notion as n on g.id = n.graph_id "
            + "where nf.id = :notionFolderId", nativeQuery = true)
    List<Notion> findAllInNotionFolder(Long notionFolderId);
}
