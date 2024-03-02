package rmap.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RelatedNotionInfo {

    @NotNull(message = "RelatedNotionInfo.id = null")
    private Long id;

    private String relevance;
    private String reverseRelevance;


    public RelatedNotionInfo(Long id, String relevance, String reverseRelevance) {
        this.id = id;
        this.relevance = relevance;
        this.reverseRelevance = reverseRelevance;
    }
}
