package rmap.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import rmap.entity.Edge;
import rmap.entity.Notion;
import rmap.exception.BusinessRuleException;
import rmap.exception.type.EdgeExceptionType;
import rmap.repository.EdgeRepository;

@Service
@RequiredArgsConstructor
public class EdgeService {

    private final EdgeRepository edgeRepository;

    public Edge connect(Notion sourceNotion, Notion targetNotion, String description) {
        Edge edge = new Edge(sourceNotion, targetNotion, description);
        Edge savedEdge = edgeRepository.save(edge);
        sourceNotion.addEdge(savedEdge);
        return savedEdge;

    }

    public void disconnect(Notion sourceNotion, Notion targetNotion) {
        if (sourceNotion.equals(targetNotion)) {
            throw new BusinessRuleException(EdgeExceptionType.SELF_LOOP);
        }
        Edge edge = edgeRepository.findByNotionIds(sourceNotion.getId(), targetNotion.getId());
        edgeRepository.delete(edge);
        sourceNotion.removeEdge(edge);
    }

    public void editDescription(Notion sourceNotion, Notion targetNotion, String description) {
        Edge edge = sourceNotion.findEdge(targetNotion);
        edge.changeDescription(description);
    }

    public List<Edge> findAllByNotionId(Long notionId) {
        return edgeRepository.findAllByNotionId(notionId);
    }

    public void deleteEdges(List<Edge> edges) {
        edgeRepository.deleteAllInBatch(edges);
    }

}
