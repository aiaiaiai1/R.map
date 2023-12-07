package rmap.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rmap.entity.Edge;

public interface EdgeRepository extends JpaRepository<Edge, Long> {
}
