package rmap.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
    @JoinColumn(name = "notion_folder_id", nullable = false)
    private NotionFolder notionFolder;

    @Column(length = 100, nullable = false)
    private String name;

    @Column(length = 2000, nullable = false)
    @ColumnDefault("''")
    private String content;

    @Embedded
    private SourceEdges edges;

    public Notion(String name, String content, NotionFolder notionFolder) {
        validateInit(name, content);
        validateNotionFolder(notionFolder);
        this.name = name;
        this.content = content;
        this.notionFolder = notionFolder;
        this.edges = new SourceEdges();
    }

    private void validateInit(String name, String content) {
        Assert.notNull(name, "name is null");
        Assert.notNull(content, "content is null");
    }

    private void validateNotionFolder(NotionFolder notionFolder) {
        Assert.notNull(notionFolder, "notionFolder is null");
        Assert.notNull(notionFolder.getId(), "notionFolder.id is null");
    }

    public Edge connect(Notion targetNotion, String description) {
        return edges.addNewEdge(this, targetNotion, description);
    }

    public void disconnect(Notion targetNotion) {
        edges.removeEdge(targetNotion);
    }

    public void editDescription(Notion targetNotion, String description) {
        edges.editDescription(targetNotion, description);
    }

    public boolean isInSameNotionFolder(Notion notion) {
        return this.notionFolder.equals(notion.getNotionFolder());
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

    public List<Edge> getEdges() {
        return edges.getEdges();
    }
}
