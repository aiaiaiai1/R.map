package rmap.response;


import lombok.Getter;
import rmap.entity.NotionFolder;

@Getter
public class NotionFolderResponse {
    private final Long id;
    private final String name;

    public NotionFolderResponse(NotionFolder notionFolder) {
        this.id = notionFolder.getId();
        this.name = notionFolder.getName();
    }
}
