package rmap.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rmap.entity.Graph;
import rmap.entity.Notion;
import rmap.response.GraphResponse;
import rmap.response.NotionCompactResponse;
import rmap.service.GraphService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/graphs")
public class GraphController {

    private final GraphService graphService;

    @GetMapping
    public ResponseEntity<List<GraphResponse>> getGraphs() {
        List<Graph> graphs = graphService.readAll();
        List<GraphResponse> responses = graphs.stream()
                .map(GraphResponse::new)
                .toList();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<NotionCompactResponse>> getGraphNotions(@PathVariable("id") Long graphId) {
        List<Notion> notions = graphService.readNotionsOfGraph(graphId);
        List<NotionCompactResponse> responses = notions.stream()
                .map(NotionCompactResponse::new)
                .toList();
        return ResponseEntity.ok(responses);
    }
}
