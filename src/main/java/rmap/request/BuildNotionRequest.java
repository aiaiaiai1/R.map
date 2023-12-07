package rmap.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class BuildNotionRequest {

    private final String name;
    private final String content;
    private final RelatedNotionInfo relatedNotion;

}

