package rmap.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import rmap.entity.Notion;

@Getter
@RequiredArgsConstructor
public class NotionCompactResponse {
    private final Long id;
    private final String name;

    public NotionCompactResponse(Notion notion) {
        this(notion.getId(), notion.getName());
    }
}
