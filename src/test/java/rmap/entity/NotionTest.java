package rmap.entity;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static rmap.EntityCreationSupporter.그래프_생성;
import static rmap.EntityCreationSupporter.노션_폴더_생성;

class NotionTest {

    @Nested
    class 노션_생성 {
        @Test
        void 노션을_생성한다() {
            // given
            NotionFolder notionFolder = 노션_폴더_생성(1L, "폴더");
            Graph graph = 그래프_생성(1L, notionFolder);

            // when, then
            Notion notion = new Notion("개념", "내용", graph);
        }

        @Test
        void 그래프가_null_인_경우_예외가_발생한다() {
            // given
            Graph graph = null;

            // when, then
            assertThatThrownBy(() -> new Notion("개념", "내용", graph))
                    .isInstanceOf(IllegalArgumentException.class);

        }

        @Test
        void 그래프의_id가_null_인_경우_예외가_발생한다() {
            // given
            NotionFolder notionFolder = 노션_폴더_생성(1L, "폴더");
            Graph graph = 그래프_생성(null, notionFolder);

            // when, then
            assertThatThrownBy(() -> new Notion("개념", "내용", graph))
                    .isInstanceOf(IllegalArgumentException.class);

        }
    }

}
