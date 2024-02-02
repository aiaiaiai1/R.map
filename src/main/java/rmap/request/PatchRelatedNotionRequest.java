package rmap.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class PatchRelatedNotionRequest {

    @NotNull(message = "related_notion_id is null")
    private Long id;
    private String name;
    private String relevance;
    private String reverseRelevance;
}

