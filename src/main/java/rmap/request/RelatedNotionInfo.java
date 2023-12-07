package rmap.request;

import lombok.Getter;

@Getter
public class RelatedNotionInfo {

    private Long id;

    public RelatedNotionInfo(Long id) {
        this.id = id;
    }

    public RelatedNotionInfo() {
    }
}
