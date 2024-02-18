package rmap.entity;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static rmap.EntityCreationSupporter.노션_폴더_생성;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class NotionTest {

    @Nested
    class 노션_생성 {
        @Test
        void 노션을_생성한다() {
            // given
            NotionFolder notionFolder = 노션_폴더_생성(1L, "폴더");

            // when, then
            Notion notion = new Notion("개념", "내용", notionFolder);
        }

        @Test
        void 노션_폴더가_null_인_경우_예외가_발생한다() {
            // given
            NotionFolder notionFolder = null;

            // when, then
            assertThatThrownBy(() -> new Notion("개념", "내용", notionFolder))
                    .isInstanceOf(IllegalArgumentException.class);

        }

        @Test
        void 노션_폴더의_id가_null_인_경우_예외가_발생한다() {
            // given
            NotionFolder notionFolder = 노션_폴더_생성(null, "폴더");

            // when, then
            assertThatThrownBy(() -> new Notion("개념", "내용", notionFolder))
                    .isInstanceOf(IllegalArgumentException.class);

        }
    }

}
