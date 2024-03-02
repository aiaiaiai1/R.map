package rmap.response;

import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import rmap.entity.Notion;

@Getter
@RequiredArgsConstructor
public class GraphResponse {

    private final List<Long> notionIds;

    public static GraphResponse of(List<Notion> graph) {
        return new GraphResponse(
                graph.stream()
                        .map(Notion::getId)
                        .toList()
        );
    }
}
