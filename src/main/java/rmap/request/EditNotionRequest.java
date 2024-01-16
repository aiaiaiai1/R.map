package rmap.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class EditNotionRequest {

    @NotBlank(message = "notion.name is blank")
    private String name;

    @NotNull(message = "notion.content is null")
    private String content;

    public EditNotionRequest() {
    }

    public EditNotionRequest(String name, String content) {
        this.name = name;
        this.content = content;
    }

}


