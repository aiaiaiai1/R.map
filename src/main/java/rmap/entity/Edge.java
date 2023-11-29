package rmap.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Edge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "source_notion_id", nullable = false)
    private Notion sourceNotion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_notion_id", nullable = false)
    private Notion targetNotion;

    @Column(length = 70)
    private String description;

    public Edge(Notion sourceNotion, Notion targetNotion, String description) {
        this.sourceNotion = sourceNotion;
        this.targetNotion = targetNotion;
        this.description = description;
    }

}
