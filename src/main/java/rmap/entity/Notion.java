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
import java.util.Optional;
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
    @JoinColumn(name = "notion_folder_id", nullable = false)
    private NotionFolder notionFolder;

    @Column(length = 100, nullable = false)
    private String name;

    @Column(length = 2000, nullable = false)
    @ColumnDefault("''")
    private String content;

    @OneToMany(mappedBy = "sourceNotion", orphanRemoval = true)
    private List<Edge> edges = new ArrayList<>();

    public Notion(String name, String content, NotionFolder notionFolder) {
        validateInit(name, content);
        validateNotionFolder(notionFolder);
        this.name = name;
        this.content = content;
        this.notionFolder = notionFolder;
    }

    private void validateInit(String name, String content) {
        Assert.notNull(name, "name is null");
        Assert.notNull(content, "content is null");
    }

    private void validateNotionFolder(NotionFolder notionFolder) {
        Assert.notNull(notionFolder, "notionFolder is null");
        Assert.notNull(notionFolder.getId(), "notionFolder.id is null");
    }

    public void addEdge(Edge edge) {
        if (edge.getId() == null) {
            throw new IllegalArgumentException("edge.id is null");
        }
        if (!edge.getSourceNotion().equals(this)) {
            throw new IllegalArgumentException("시작엣지만 넣을 수 있습니다.");
        }
        if (edges.contains(edge)) {
            return;
        }
        edges.add(edge);
    }

    public Edge connect(Notion targetNotion, String description) {
        Edge edge = new Edge(this, targetNotion, description);
        if (edges.contains(edge)) {
            throw new IllegalArgumentException("이미 존재하는 엣지 입니다.");
        }
        edges.add(edge);
        return edge;
    }

    public void disconnect(Notion targetNotion) {
        Optional<Edge> edge = findEdge(targetNotion);
        if (edge.isEmpty()) {
            throw new IllegalArgumentException("삭제하려고 하는 엣지가 없는 엣지 입니다.");
        }
        edges.remove(edge.get());
    }

    public void editDescription(Notion targetNotion, String description) {
        Optional<Edge> edge = findEdge(targetNotion);
        if (edge.isEmpty()) {
            throw new IllegalArgumentException("존재하지 않는 엣지 입니다.");
        }
        edge.get().changeDescription(description);
    }

    public boolean isInSameNotionFolder(Notion notion) {
        return this.notionFolder.equals(notion.getNotionFolder());
    }

    public Optional<Edge> findEdge(Notion targetNotion) {
        return edges.stream()
                .filter(e -> e.getTargetNotion().equals(targetNotion))
                .findAny();
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

    public void changeNotionFolder(NotionFolder notionFolder) {
        validateNotionFolder(notionFolder);
        this.notionFolder = notionFolder;
    }
}
