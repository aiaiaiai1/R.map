package rmap.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BuildNotionRequest {

    @NotNull(message = "notionFolderId is null")
    private Long notionFolderId;

    @NotBlank(message = "notion.name is null")
    private String name;

    private String content;

    private RelatedNotionInfo relatedNotion;

    public BuildNotionRequest(Long notionFolderId, String name, String content, RelatedNotionInfo relatedNotion) {
        this.notionFolderId = notionFolderId;
        this.name = name;
        this.content = content;
        this.relatedNotion = relatedNotion;
    }
}

