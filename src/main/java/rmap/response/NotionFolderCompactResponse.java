package rmap.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import rmap.entity.NotionFolder;

@Getter
@RequiredArgsConstructor
public class NotionFolderCompactResponse {
    private final Long id;
    private final String name;
    private final OwnerResponse owner;
    private final Boolean isPrivate;

    public NotionFolderCompactResponse(NotionFolder notionFolder) {
        this.id = notionFolder.getId();
        this.name = notionFolder.getName();
        this.owner = new OwnerResponse(notionFolder.getOwner());
        this.isPrivate = notionFolder.isPrivate();
    }

}
