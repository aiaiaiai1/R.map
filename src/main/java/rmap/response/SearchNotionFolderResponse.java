package rmap.response;

import lombok.Getter;
import rmap.entity.Notion;
import rmap.entity.NotionFolder;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class SearchNotionFolderResponse {
    private final Long id;
    private final String name;
    private final Long ownerId;
    private final List<NotionCompactResponse> content;

    public SearchNotionFolderResponse(NotionFolder notionFolder, List<Notion> notions) {
        this.id = notionFolder.getId();
        this.name = notionFolder.getName();
        this.ownerId = notionFolder.getOwner().getId();
        this.content = notions.stream().map(NotionCompactResponse::new).toList();
    }


}
