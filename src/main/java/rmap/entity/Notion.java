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

    @OneToMany(mappedBy = "sourceNotion", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Edge> edges = new ArrayList<>();

    // 왜냐하면 타겟노션도 다 삭제해야함. 저장에는 케스케이드 사용이 불필요. 고아 객체는 사용할만한듯?-> 고아객체도 양쪽에다가 제거해줘야하네
// 99% 양뱡향 매핑 없애기ㅣ...
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

//    public void connect(Edge edge) {
//        validateConnection(edge);
//        edges.add(edge);
//    }
//
//    private void validateConnection(Edge edge) {
//        if (!this.equals(edge.getSourceNotion())) {
//            throw new IllegalArgumentException("출발 노선이 일치 하지 않습니다.");
//        }
//
//        if (edges.contains(edge)) {
//            throw new IllegalArgumentException("이미 추가되어있습니다");
//        }
//    }

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
