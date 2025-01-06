package rmap.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PriavteOrNotRequest {

    @NotNull
    private Boolean isPrivate;

    public PriavteOrNotRequest(Boolean isPrivate) {
        this.isPrivate = isPrivate;
    }
}


