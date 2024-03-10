package rmap.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SplitNotionFolderRequest {

    @NotBlank(message = "notionFolder.name is null")
    private String name;

    public SplitNotionFolderRequest(String name) {
        this.name = name;
    }

}
