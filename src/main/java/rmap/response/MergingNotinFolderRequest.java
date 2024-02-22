package rmap.response;

import java.util.List;
import lombok.Getter;

@Getter
public class MergingNotinFolderRequest {

    private String name;
    private List<Long> notionFolderIds;

    public MergingNotinFolderRequest() {
    }

    public MergingNotinFolderRequest(String name, List<Long> notionFolderIds) {
        this.name = name;
        this.notionFolderIds = notionFolderIds;
    }
}
