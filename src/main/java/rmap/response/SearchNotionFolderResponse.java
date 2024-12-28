package rmap.response;

import lombok.Getter;
import rmap.entity.Notion;
import rmap.entity.NotionFolder;

import java.util.List;

@Getter
public class SearchNotionFolderResponse {
    private final Long id;
    private final String name;
    private final OwnerResponse owner;
    private final List<NotionCompactResponse> content;

    public SearchNotionFolderResponse(NotionFolder notionFolder, List<Notion> notions) {
        this.id = notionFolder.getId();
        this.name = notionFolder.getName();
        this.owner = new OwnerResponse(notionFolder.getOwner());
        this.content = notions.stream().map(NotionCompactResponse::new).toList();
    }


}
