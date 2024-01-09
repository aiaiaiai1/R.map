package rmap.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Getter
public class BuildNotionRequest {

    private Long notionFolderId;
    private String name;
    private String content;
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

