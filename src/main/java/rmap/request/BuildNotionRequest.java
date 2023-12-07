package rmap.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Getter
@AllArgsConstructor
public class BuildNotionRequest {

    private String name;
    private String content;
    private RelatedNotionInfo relatedNotion;

    public BuildNotionRequest() {
    }

}

