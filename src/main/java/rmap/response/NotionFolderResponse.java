package rmap.response;

import java.util.List;
import lombok.Getter;
import rmap.entity.Notion;
import rmap.entity.NotionFolder;

@Getter
public class NotionFolderResponse {

    private final Long id;
    private final String name;
    private final List<NotionCompactResponse> notions;

    public NotionFolderResponse(Long id, String name, List<NotionCompactResponse> notions) {
        this.id = id;
        this.name = name;
        this.notions = notions;
    }

    public static NotionFolderResponse of(NotionFolder notionFolder, List<Notion> notions) {
        return new NotionFolderResponse(
                notionFolder.getId(),
                notionFolder.getName(),
                notions.stream()
                        .map(NotionCompactResponse::new)
                        .toList()
        );
    }
}
