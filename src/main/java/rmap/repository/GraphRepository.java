package rmap.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rmap.entity.Graph;

@Repository
public interface GraphRepository extends JpaRepository<Graph, Long> {
}
