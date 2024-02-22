package rmap.response;

import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import rmap.entity.Notion;

@Getter
@RequiredArgsConstructor
public class GraphResponse {

    private final Long id;
    private final List<Long> notionIds;

    public static GraphResponse of(Long id, List<Notion> graph) {
        return new GraphResponse(id, graph.stream().map(Notion::getId).toList());
    }
}
