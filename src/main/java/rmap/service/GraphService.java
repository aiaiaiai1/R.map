package rmap.service;

import java.util.Comparator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import rmap.entity.Graph;
import rmap.entity.Notion;
import rmap.entity.NotionFolder;
import rmap.exception.type.GraphExceptionType;
import rmap.exception.NotFoundException;
import rmap.repository.GraphRepository;

@Service
@RequiredArgsConstructor
public class GraphService {

    private final GraphRepository graphRepository;

    public Graph createGraph(NotionFolder notionFolder) {
        Graph graph = new Graph(notionFolder);
        return graphRepository.save(graph);
    }

    public List<Notion> readNotionsOfGraph(Long graphId) {
        Graph graph = graphRepository.findById(graphId)
                .orElseThrow(() -> new NotFoundException(GraphExceptionType.NOT_FOUND));
        List<Notion> notions = graph.getNotions();
        return notions.stream()
                .sorted(Comparator.comparing(Notion::getName))
                .toList();
    }

}
