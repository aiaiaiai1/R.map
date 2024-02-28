package rmap.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PatchRelatedNotionRequest {

    @NotNull(message = "related_notion_id is null")
    private Long id;
    private String relevance;
    private String reverseRelevance;

    public PatchRelatedNotionRequest(Long id, String relevance, String reverseRelevance) {
        this.id = id;
        this.relevance = relevance;
        this.reverseRelevance = reverseRelevance;
    }
}

