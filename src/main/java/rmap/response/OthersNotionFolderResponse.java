package rmap.response;

import lombok.Getter;
import rmap.entity.User;

import java.util.List;

@Getter
public class OthersNotionFolderResponse {
    private final Long ownerId;
    private final String ownerName;
    private final List<NotionFolderResponse> folderList;

    public OthersNotionFolderResponse(User owner, List<NotionFolderResponse> folderList) {
        this.ownerId = owner.getId();
        this.ownerName = owner.getNickname();
        this.folderList = folderList;
    }

}
