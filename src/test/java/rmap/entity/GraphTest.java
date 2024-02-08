package rmap.entity;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static rmap.EntityCreationSupporter.노션_폴더_생성;

import org.junit.jupiter.api.Test;

class GraphTest {

    @Test
    void 그래프를_생성한다() {
        // given
        NotionFolder notionFolder = 노션_폴더_생성(1L, "폴더");

        // when, then
        Graph graph = new Graph(notionFolder);

    }

    @Test
    void 노션_폴더_id_가_null_인_경우_예외가_발생한다() {
        // given
        NotionFolder notionFolder = 노션_폴더_생성(null, "폴더");

        // when, then
        assertThatThrownBy(() -> new Graph(notionFolder))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 노션_폴더가_null_인_경우_예외가_발생한다() {
        // given
        NotionFolder notionFolder = null;

        // when, then
        assertThatThrownBy(() -> new Graph(notionFolder))
                .isInstanceOf(IllegalArgumentException.class);
    }

}
