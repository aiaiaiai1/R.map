package rmap.entity;

import jakarta.persistence.Embeddable;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.Getter;

@Embeddable
@Getter
public class SourceEdges {

    @OneToMany(mappedBy = "sourceNotion", orphanRemoval = true)
    private List<Edge> edges = new ArrayList<>();

    protected SourceEdges() {
    }

    protected SourceEdges(List<Edge> edges) {
        this.edges = new ArrayList<>(edges);
    }

    public Edge addNewEdge(Notion sourceNotion, Notion targetNotion, String description) {
        Edge edge = new Edge(sourceNotion, targetNotion, description);
        if (edges.contains(edge)) {
            throw new IllegalArgumentException("이미 존재하는 엣지 입니다.");
        }
        edges.add(edge);
        return edge;
    }

    public void removeEdge(Notion targetNotion) {
        findEdge(targetNotion)
                .ifPresent(edge -> edges.remove(edge));
    }

    private Optional<Edge> findEdge(Notion targetNotion) {
        return edges.stream()
                .filter(e -> e.getTargetNotion().equals(targetNotion))
                .findAny();
    }

    public void editDescription(Notion targetNotion, String description) {
        Optional<Edge> edge = findEdge(targetNotion);
        if (edge.isEmpty()) {
            throw new IllegalArgumentException("존재하지 않는 엣지 입니다.");
        }
        edge.get().changeDescription(description);
    }

    public List<Edge> getEdges() {
        return List.copyOf(edges);
    }
}
