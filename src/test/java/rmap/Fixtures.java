package rmap;

import static rmap.EntityCreationSupporter.노션_폴더_생성;

import rmap.entity.NotionFolder;

public class Fixtures {

    public static final NotionFolder 노션_폴더_음식 = 노션_폴더_생성(1L, "음식");
    public static final NotionFolder 노션_폴더_알파벳 = 노션_폴더_생성(2L, "알파벳");

    private Fixtures() {
    }

}
