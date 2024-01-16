package rmap.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class BuildNotionRequest {

    @NotNull(message = "notionFolderId is null")
    private Long notionFolderId;

    @NotBlank(message = "notion.name is null")
    private String name;

    @NotNull(message = "notion.content is null")
    private String content;

    @NotNull(message = "relatedNotion is null")
    private RelatedNotionInfo relatedNotion;

    public BuildNotionRequest() {
    }

    public BuildNotionRequest(Long notionFolderId, String name, String content, RelatedNotionInfo relatedNotion) {
        this.notionFolderId = notionFolderId;
        this.name = name;
        this.content = content;
        this.relatedNotion = relatedNotion;
    }
}

