package rmap.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
        this.graph = graph;
    }

    private void validateGraph(Graph graph) {
        Assert.notNull(graph, "graph is null");
        Assert.notNull(graph.getId(), "graph.id is null");
    }

    public void editName(String name) {
        this.name = Objects.requireNonNull(name, "name is null");
    }

    public void editContent(String content) {
        this.content = Objects.requireNonNull(content, "content is null");
    }
}
