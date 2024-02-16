package rmap.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class RelatedNotionResponse {

    private final Long id;
    private final String name;
    private final String relevance;
    private final String reverseRelevance;

}
