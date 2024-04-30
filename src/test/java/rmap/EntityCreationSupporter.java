package rmap;

import java.time.LocalDateTime;
import org.springframework.test.util.ReflectionTestUtils;
import rmap.entity.Edge;
import rmap.entity.Notion;
import rmap.entity.NotionFolder;
import rmap.entity.User;

public class EntityCreationSupporter {

    private EntityCreationSupporter() {
    }

    public static Notion 노션_생성(Long id, String name, String content, NotionFolder notionFolder) {
        Notion notion = new Notion(name, content, notionFolder);
        ReflectionTestUtils.setField(notion, "id", id);
        return notion;
    }

    public static NotionFolder 노션_폴더_생성(Long id, User user, String name) {
        NotionFolder notionFolder = new NotionFolder(user, name);
        ReflectionTestUtils.setField(notionFolder, "id", id);
        return notionFolder;
    }

    public static Edge 엣지_생성(Long id, Notion sourceNotion, Notion targetNotion, String description) {
        Edge edge = new Edge(sourceNotion, targetNotion, description);
        ReflectionTestUtils.setField(edge, "id", id);
        return edge;
    }

    public static User 유저_생성(Long id, String email, String password, LocalDateTime createdAt) {
        User user = new User(email, password, createdAt);
        ReflectionTestUtils.setField(user, "id", id);
        return user;
    }

    public static User 유저_생성(Long id, String email, String password) {
        User user = new User(email, password);
        ReflectionTestUtils.setField(user, "id", id);
        return user;
    }

}
