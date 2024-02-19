package rmap.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Getter
@NoArgsConstructor
public class NotionFolderRequest {

    @NotBlank(message = "notionFolder.name is blank")
    private String name;

    public NotionFolderRequest(String name) {
        this.name = name;
    }
}
