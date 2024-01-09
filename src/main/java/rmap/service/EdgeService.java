package rmap.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import rmap.entity.Edge;
import rmap.entity.Notion;
import rmap.repository.EdgeRepository;

@Service
@RequiredArgsConstructor
public class EdgeService {

    private final EdgeRepository edgeRepository;

    public Edge connect(Notion sourceNotion, Notion targetNotion, String description) {
        Edge edge = new Edge(sourceNotion, targetNotion, description);
        return edgeRepository.save(edge);
    }

    public List<Edge> findAllByNotionId(Long notionId) {
        return edgeRepository.findAllByNotionId(notionId);
    }

    public void deleteEdges(List<Edge> edges) {
        edgeRepository.deleteAllInBatch(edges);
    }

}
