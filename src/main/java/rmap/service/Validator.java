package rmap.service;

import rmap.entity.Notion;
import rmap.entity.NotionFolder;
import rmap.entity.User;

public final class Validator {

    private Validator() {
    }

    public static void validateNotionFolderOwner(NotionFolder notionFolder, User user) {
        if (!notionFolder.isOwner(user)) {
            throw new IllegalArgumentException("권한이 없습니다.");
        }
    }

    public static void validateNotionOwner(Notion notion, User user) {
        NotionFolder notionFolder = notion.getNotionFolder();
        validateNotionFolderOwner(notionFolder,user);
    }
}
