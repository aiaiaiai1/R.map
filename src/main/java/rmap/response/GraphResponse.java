package rmap.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import rmap.entity.Graph;

@Getter
@RequiredArgsConstructor
public class GraphResponse {
    private final Long id;
    private final String name;

    public GraphResponse(Graph graph) {
        this(graph.getId(), graph.getName());
    }
}
