package rmap.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = {"sourceNotion", "targetNotion"})
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"source_notion_id", "target_notion_id"}))
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

     Edge(Notion sourceNotion, Notion targetNotion, String description) {
        validateNotions(sourceNotion, targetNotion);
        this.sourceNotion = sourceNotion;
        this.targetNotion = targetNotion;
        this.description = description;
    }

    private void validateNotions(Notion sourceNotion, Notion targetNotion) {
        Assert.notNull(sourceNotion, "1");
        Assert.notNull(targetNotion, "2");
        Assert.notNull(sourceNotion.getId(), "3");
        Assert.notNull(targetNotion.getId(), "4");
        if (sourceNotion.equals(targetNotion)) {
            throw new IllegalArgumentException("5");
        }
    }

}
