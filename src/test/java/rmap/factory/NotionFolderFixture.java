package rmap.factory;

import rmap.entity.NotionFolder;
import rmap.entity.User;

public enum NotionFolderFixture {

    DEFAULT(-1L, "노션 폴더 이름", "내용", UserFixture.DEFAULT.user()),
    DEFAULT_1(-2L, "", "", UserFixture.DEFAULT_1.user()),
    ;

    private final Long id;
    private final String name;
    private final String content;
    private final User creator;

    NotionFolderFixture(Long id, String name, String content, User creator) {
        this.id = id;
        this.name = name;
        this.content = content;
        this.creator = creator;
    }

    public NotionFolder notionFolder() {
        return NotionFolderFactory.builder()
                .withId(id)
                .notionFolderName(name)
                .creator(creator)
                .create();
    }


}
