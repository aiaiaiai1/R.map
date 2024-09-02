package rmap.factory;

import rmap.entity.Notion;

public enum NotionFixture {

    DEFAULT(-1L, "노션", "내용"),
    A(-2L, "a", "a"),
    B(-3L, "b", "b"),
    ;

    private final Long id;
    private final String name;
    private final String content;

    NotionFixture(Long id, String name, String content) {
        this.id = id;
        this.name = name;
        this.content = content;
    }

    public Notion notion() {
        return NotionFactory.builder()
                .withId(id)
                .name(name)
                .content(content)
                .create();
    }

}
