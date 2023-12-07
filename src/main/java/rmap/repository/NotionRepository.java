package rmap.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rmap.entity.Notion;

public interface NotionRepository extends JpaRepository<Notion, Long> {

}
