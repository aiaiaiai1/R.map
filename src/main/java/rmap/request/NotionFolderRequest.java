package rmap.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class NotionFolderRequest {
    private String name;

    public NotionFolderRequest(String name) {
        this.name = name;
    }
}
