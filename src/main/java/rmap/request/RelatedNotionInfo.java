package rmap.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class RelatedNotionInfo {

    @NotNull(message = "RelatedNotionInfo.id = null")
    private Long id;

    public RelatedNotionInfo(Long id) {
        this.id = id;
    }

    public RelatedNotionInfo() {
    }
}
