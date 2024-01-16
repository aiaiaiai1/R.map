package rmap.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import rmap.entity.NotionFolder;

@Getter
@RequiredArgsConstructor
public class NotionFolderCompactResponse {
    private final Long id;
    private final String name;

    public NotionFolderCompactResponse(NotionFolder notionFolder) {
        this.id = notionFolder.getId();
        this.name = notionFolder.getName();
    }

}
