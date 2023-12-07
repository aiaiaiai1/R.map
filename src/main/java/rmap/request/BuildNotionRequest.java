package rmap.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@NoArgsConstructor
public class BuildNotionRequest {

    private String name;
    private String content;
    private RelatedNotionInfo relatedNotion;

}

