package rmap;

import static rmap.EntityCreationSupporter.노션_폴더_생성;
import static rmap.EntityCreationSupporter.유저_생성;

import rmap.entity.NotionFolder;
import rmap.entity.User;

public class Fixtures {

    public static final String TEST_EMAIL = "test123@gmail.com";
    public static final String TEST_EMAIL_1 = "test1234@gmail.com";
    public static final String TEST_PASSWORD = "abcd1234!";
    public static final User 알맵이 = 유저_생성(1000L, "user123@gmail.com", "user1234!");
    public static final NotionFolder 알맵이의_노션_폴더_알파벳 = 노션_폴더_생성(2000L, 알맵이, "알파벳");
    public static final NotionFolder 알맵이의_노션_폴더_음식 = 노션_폴더_생성(1000L, 알맵이, "음식");

    private Fixtures() {
    }

}
