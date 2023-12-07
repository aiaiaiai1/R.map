package rmap.response;

import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import rmap.entity.Notion;

@Getter
@RequiredArgsConstructor
public class NotionResponse {

    private final Long id;
    private final String name;
    private final String content;
    private final List<RelatedNotionResponse> relatedNotions;

    public static NotionResponse from(Notion notion) {
        List<RelatedNotionResponse> responses = notion.getEdges().stream()
                .map(edge -> edge.getTargetNotion())
                .map(rn -> new RelatedNotionResponse(rn.getId(), rn.getName()))
                .toList();

        return new NotionResponse(
                notion.getId(), notion.getName(), notion.getContent(), responses
        );
    }

}
