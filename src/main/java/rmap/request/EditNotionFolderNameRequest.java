package rmap.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class EditNotionFolderNameRequest {

    private String name;

    public EditNotionFolderNameRequest(String name) {
        this.name = name;
    }
}
