package rmap.response;

import lombok.Getter;
import rmap.entity.Notion;
import rmap.entity.NotionFolder;
import rmap.entity.User;

import java.util.List;

@Getter
public class OpenNotionFolderResponse {

    private final Long id;
    private final String name;
    private final OwnerResponse owner;
    private final Boolean isPrivate;
    private final List<NotionCompactResponse> notions;

    public OpenNotionFolderResponse(Long id, String name, User owner, boolean isPrivate, List<NotionCompactResponse> notions) {
        this.id = id;
        this.name = name;
        this.owner = new OwnerResponse(owner);
        this.isPrivate = isPrivate;
        this.notions = notions;
    }

    public static OpenNotionFolderResponse of(NotionFolder notionFolder, List<Notion> notions) {
        return new OpenNotionFolderResponse(
                notionFolder.getId(),
                notionFolder.getName(),
                notionFolder.getOwner(),
                notionFolder.isPrivate(),
                notions.stream()
                        .map(NotionCompactResponse::new)
                        .toList()
        );
    }
}
