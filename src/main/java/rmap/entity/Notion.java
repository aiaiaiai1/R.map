package rmap.entity;

import jakarta.persistence.CascadeType;
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
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.util.Assert;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "id")
public class Notion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "graph_id", nullable = false)
    private Graph graph;

    @Column(length = 30, nullable = false)
    private String name;

    @Column(length = 200, nullable = false)
    @ColumnDefault("")
    private String content;

    @OneToMany(mappedBy = "sourceNotion", cascade = CascadeType.ALL, orphanRemoval = true)
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

    public void connect(Edge edge) {
        validateConnection(edge);
        edges.add(edge);
    }

    private void validateConnection(Edge edge) {
        if (!this.equals(edge.getSourceNotion())) {
            throw new IllegalArgumentException("출발 노선이 일치 하지 않습니다.");
        }

        if (edges.contains(edge)) {
            throw new IllegalArgumentException("이미 추가되어있습니다");
        }
    }

    public void changeGraph(Graph graph) {
        validateGraph(graph);
        this.graph = graph;
    }

    private void validateGraph(Graph graph) {
        Assert.notNull(graph, "graph is null");
        Assert.notNull(graph.getId(), "graph.id is null");
    }
}
