package rmap.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.util.Assert;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = {"id"})
public class Notion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "graph_id", nullable = false)
    private Graph graph;

    @Column(length = 100, nullable = false)
    private String name;

    @Column(length = 2000, nullable = false)
    @ColumnDefault("''")
    private String content;

    @OneToMany(mappedBy = "sourceNotion", orphanRemoval = true)
    private List<Edge> edges = new ArrayList<>();

    public Notion(String name, String content, Graph graph) {
        validateInit(name, content);
        validateGraph(graph);
        this.name = name;
        this.content = content;
        this.graph = graph;
    }

    private void validateInit(String name, String content) {
        Assert.notNull(name, "name is null");
        Assert.notNull(content, "content is null");
    }

    public void changeGraph(Graph graph) {
        validateGraph(graph);
        this.graph.remove(this);
        this.graph = graph;
        graph.add(this);
    }

    private void validateGraph(Graph graph) {
        Assert.notNull(graph, "graph is null");
        Assert.notNull(graph.getId(), "graph.id is null");
        if (!this.graph.getNotionFolder().equals(graph.getNotionFolder())) {
            throw new IllegalArgumentException("같은 노션폴더에 있어야 함.");
        }
    }

    public void addEdge(Edge edge) {
        if (edges.contains(edge)) {
            throw new IllegalArgumentException("이미 존재하는 Edge, 연관관계 편의 메서드");
        }
        edges.add(edge);
    }

    public void removeEdge(Edge edge) {
        edges.remove(edge);
    }

    public void editName(String name) {
        this.name = Objects.requireNonNull(name, "name is null");
    }

    public void editContent(String content) {
        this.content = Objects.requireNonNull(content, "content is null");
    }
}
