package rmap.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rmap.entity.Graph;
import rmap.exception.NotFoundException;
import rmap.exception.type.GraphExceptionType;

@Repository
public interface GraphRepository extends JpaRepository<Graph, Long> {

    default Graph findByIdOrThrow(Long graphId) {
        Graph graph = findById(graphId)
                .orElseThrow(() -> new NotFoundException(GraphExceptionType.NOT_FOUND));
        return graph;
    }

}
