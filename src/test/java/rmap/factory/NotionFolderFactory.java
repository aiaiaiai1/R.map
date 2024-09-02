package rmap.factory;

import org.springframework.test.util.ReflectionTestUtils;
import rmap.entity.NotionFolder;
import rmap.entity.User;

public class NotionFolderFactory {

    private NotionFolderFactory() {
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long id;
        private User owner;
        private String name;

        public Builder withId(Long id) {
            this.id = id;
            return this;
        }

        public Builder creator(User creator) {
            this.owner = creator;
            return this;
        }

        public Builder notionFolderName(String notionFolderName) {
            this.name = notionFolderName;
            return this;
        }

        public NotionFolder create() {
            NotionFolder notionFolder = new NotionFolder(owner, name);
            ReflectionTestUtils.setField(notionFolder, "id", id);
            return notionFolder;
        }

    }


}
