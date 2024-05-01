package rmap.response;

import java.util.List;
import lombok.Getter;
import rmap.entity.Notion;
import rmap.entity.NotionFolder;

@Getter
public class OpenNotionFolderResponse {

    private final Long id;
    private final String name;
    private final List<NotionCompactResponse> notions;

    public OpenNotionFolderResponse(Long id, String name, List<NotionCompactResponse> notions) {
        this.id = id;
        this.name = name;
        this.notions = notions;
    }

    public static OpenNotionFolderResponse of(NotionFolder notionFolder, List<Notion> notions) {
        return new OpenNotionFolderResponse(
                notionFolder.getId(),
                notionFolder.getName(),
                notions.stream()
                        .map(NotionCompactResponse::new)
                        .toList()
        );
    }
}
