package rmap.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.List;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = {"id"})
public class Graph {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "notion_folder_id", nullable = false)
    private NotionFolder notionFolder;

    @OneToMany(mappedBy = "graph", cascade = CascadeType.REMOVE)
    private List<Notion> notions;

    public Graph(NotionFolder notionFolder) {
        Assert.notNull(notionFolder, "notionFolder is null");
        Assert.notNull(notionFolder.getId(), "notionFolder.id is null");
        this.notionFolder = notionFolder;
    }

}