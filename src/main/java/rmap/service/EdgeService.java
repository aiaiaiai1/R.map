package rmap.service;

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

}
