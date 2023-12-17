package rmap.entity;

import org.springframework.test.util.ReflectionTestUtils;

public class EntityCreationSupporter {

    private EntityCreationSupporter() {
    }

    public static Notion 노션_생성(Long id, String name, String content, Graph graph) {
        Notion notion = new Notion(name, content, graph);
        ReflectionTestUtils.setField(notion, "id", id);
        return notion;
    }

    public static Graph 그래프_생성(Long id, String name) {
        Graph graph = new Graph(name);
        ReflectionTestUtils.setField(graph, "id", id);
        return graph;
    }
}
