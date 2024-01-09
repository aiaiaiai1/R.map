package rmap.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import rmap.entity.Edge;

public interface EdgeRepository extends JpaRepository<Edge, Long> {

    @Query(value = "select * from edge as e "
            + "join notion as n on n.id = e.source_notion_id "
            + "or n.id = e.target_notion_id"
            + "where n.id = :notionId",nativeQuery = true)
    List<Edge> findAllByNotionId(Long notionId);

//    @Query(value = "delete from edge where id in :edgeIds", nativeQuery = true)
//    void delete(List<Long> edgeIds);

}
