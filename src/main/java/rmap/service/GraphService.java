package rmap.service;

import java.util.Comparator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import rmap.entity.Graph;
import rmap.entity.Notion;
import rmap.repository.GraphRepository;

@Service
@RequiredArgsConstructor
public class GraphService {

    private final GraphRepository graphRepository;

    public Graph createGraph(String name) {
        Graph graph = new Graph(name);
        return graphRepository.save(graph);
    }

    public List<Graph> readAll() {
        return graphRepository.findAll();
    }

    public List<Notion> readNotionsOfGraph(Long graphId) {
        Graph graph = graphRepository.findById(graphId)
                .orElseThrow(() -> new IllegalArgumentException("그래프가 존재하지 않습니다"));
        List<Notion> notions = graph.getNotions();
        return notions.stream()
                .sorted(Comparator.comparing(Notion::getName))
                .toList();
    }

}
