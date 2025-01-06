package rmap.response;

import lombok.Getter;
import rmap.entity.Notion;

@Getter
public class SearchNotionsResponse {
    private final Long id;
    private final String name;
    private final String content;
    private final NotionFolderCompactResponse notionFolder;

    public SearchNotionsResponse(Notion notion) {
        this.id = notion.getId();
        this.name = notion.getName();
        this.content = notion.getContent();
        this.notionFolder = new NotionFolderCompactResponse(notion.getNotionFolder());
    }

}
