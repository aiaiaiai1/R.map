package rmap.request;

import lombok.Getter;

@Getter
public class EditNotionRequest {

    private String name;
    private String content;

    public EditNotionRequest() {
    }

    public EditNotionRequest(String name, String content) {
        this.name = name;
        this.content = content;
    }

}


