package rmap.response;

import lombok.Getter;
import rmap.entity.Notion;
import rmap.entity.NotionFolder;

import java.util.List;

@Getter
public class OpenNotionFolderResponse {

    private final Long id;
    private final String name;
    private final Long ownerId;
    private final Boolean isPrivate;
    private final List<NotionCompactResponse> notions;

    public OpenNotionFolderResponse(Long id, String name, Long ownerId, boolean isPrivate, List<NotionCompactResponse> notions) {
        this.id = id;
        this.name = name;
        this.ownerId = ownerId;
        this.isPrivate = isPrivate;
        this.notions = notions;
    }

    public static OpenNotionFolderResponse of(NotionFolder notionFolder, List<Notion> notions) {
        return new OpenNotionFolderResponse(
                notionFolder.getId(),
                notionFolder.getName(),
                notionFolder.getOwner().getId(),
                notionFolder.isPrivate(),
                notions.stream()
                        .map(NotionCompactResponse::new)
                        .toList()
        );
    }
}
