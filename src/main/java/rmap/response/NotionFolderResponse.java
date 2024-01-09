package rmap.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import rmap.entity.NotionFolder;

@Getter
@RequiredArgsConstructor
public class NotionFolderResponse {
    private final Long id;
    private final String name;

    public NotionFolderResponse(NotionFolder notionFolder) {
        this.id = notionFolder.getId();
        this.name = notionFolder.getName();
    }

}
