package rmap;

import rmap.entity.NotionFolder;
import rmap.entity.User;

import static rmap.EntityCreationSupporter.노션_폴더_생성;
import static rmap.EntityCreationSupporter.유저_생성;

public class Fixtures {

    public static final String TEST_EMAIL = "test123@gmail.com";
    public static final String TEST_PASSWORD = "abcd1234!";
    public static final User 유저 = 유저_생성(1000L, TEST_EMAIL, TEST_PASSWORD);
    public static final NotionFolder 노션_폴더_알파벳 = 노션_폴더_생성(2000L, 유저, "알파벳");
    public static final NotionFolder 노션_폴더_음식 = 노션_폴더_생성(1000L, 유저, "음식");

    private Fixtures() {
    }

}
