package rmap.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rmap.entity.NotionFolder;

@Repository
public interface NotionFolderRepository extends JpaRepository<NotionFolder, Long> {
}
