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
import org.hibernate.annotations.ColumnDefault;
import org.springframework.util.Assert;
import rmap.exception.BusinessRuleException;
import rmap.exception.type.EdgeExceptionType;

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

    @Column(length = 200, nullable = false)
    @ColumnDefault("''")
    private String description;

    public Edge(Notion sourceNotion, Notion targetNotion, String description) {
        validateNotions(sourceNotion, targetNotion);
        Assert.notNull(description, "description is null");
        this.sourceNotion = sourceNotion;
        this.targetNotion = targetNotion;
        this.description = description;
    }

    private void validateNotions(Notion sourceNotion, Notion targetNotion) {
        Assert.notNull(sourceNotion, "sourceNotion is null");
        Assert.notNull(targetNotion, "targetNotion is null");
        Assert.notNull(sourceNotion.getId(), "sourceNotion.id is null");
        Assert.notNull(targetNotion.getId(), "targetNotion.id is null");
        if (!targetNotion.isInSameNotionFolder(sourceNotion)) {
            throw new BusinessRuleException(EdgeExceptionType.NOT_IN_SAME_NOTION_FOLDER);
        }
        if (sourceNotion.equals(targetNotion)) {
            throw new BusinessRuleException(EdgeExceptionType.SELF_LOOP);
        }
    }

    public void changeDescription(String description) {
        this.description = description;
    }

}
