package rmap.factory;

import org.springframework.test.util.ReflectionTestUtils;
import rmap.entity.Notion;
import rmap.entity.NotionFolder;

public class NotionFactory {

    private NotionFactory() {
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long id;
        private NotionFolder notionFolder;
        private String name;
        private String content;

        public Builder withId(Long id) {
            this.id = id;
            return this;
        }

        public Builder notionFolder(NotionFolder notionFolder) {
            this.notionFolder = notionFolder;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder content(String content) {
            this.content = content;
            return this;
        }

        public Notion create() {
            Notion notion = new Notion(name, content, notionFolder);
            ReflectionTestUtils.setField(notion, "id", id);
            return notion;
        }

    }

}
