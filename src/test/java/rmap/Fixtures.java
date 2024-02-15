package rmap;

import static rmap.EntityCreationSupporter.노션_폴더_생성;

import rmap.entity.NotionFolder;

public class Fixtures {

    public static final NotionFolder 노션_폴더_음식 = 노션_폴더_생성(1L, "음식");

    private Fixtures() {
    }

}
