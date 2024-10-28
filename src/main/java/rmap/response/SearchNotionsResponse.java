package rmap.response;

import lombok.Getter;
import rmap.entity.Notion;

import java.util.List;

@Getter
public class SearchNotionsResponse {
    private final Long id;
    private final String name;
    private final String content;
    private final NotionFolderCompactResponse response;

    public SearchNotionsResponse(Notion notion) {
        this.id = notion.getId();
        this.name = notion.getName();
        this.content = notion.getContent();
        this.response = new NotionFolderCompactResponse(notion.getNotionFolder());
    }

}
