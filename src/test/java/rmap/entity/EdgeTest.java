package rmap.entity;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static rmap.EntityCreationSupporter.그래프_생성;
import static rmap.EntityCreationSupporter.노션_생성;
import static rmap.EntityCreationSupporter.노션_폴더_생성;

import org.junit.jupiter.api.Test;
import rmap.exception.BusinessRuleException;

class EdgeTest {

    @Test
    void 같은_노션을_연결하는_경우_예외가_발생한다() {
        // given
        NotionFolder notionFolder = 노션_폴더_생성(1L, "폴더");
        Graph graph = 그래프_생성(1L, notionFolder);

        Notion notion = 노션_생성(1L, "개념", "내용", graph);
        Notion notion1 = 노션_생성(1L, "개념", "내용", graph);

        // when, then
        assertThatThrownBy(() -> new Edge(notion, notion1, ""))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessage("동일한 연결 지점 입니다.");
    }

    @Test
    void 노션의_id가_null_인_경우_예외가_발생한다() {
        // given
        NotionFolder notionFolder = 노션_폴더_생성(1L, "폴더");
        Graph graph = 그래프_생성(1L, notionFolder);

        Notion notion = 노션_생성(null, "개념", "내용", graph);
        Notion notion1 = 노션_생성(1L, "개념", "내용", graph);

        // when, then
        assertThatThrownBy(() -> new Edge(notion, notion1, ""))
                .isInstanceOf(IllegalArgumentException.class);

    }

    @Test
    void 노션이_null_인_경우_예외가_발생한다() {
        // given
        NotionFolder notionFolder = 노션_폴더_생성(1L, "폴더");
        Graph graph = 그래프_생성(1L, notionFolder);

        Notion notion = 노션_생성(1L, "개념", "내용", graph);
        Notion notion1 = null;

        // when, then
        assertThatThrownBy(() -> new Edge(notion, notion1, ""))
                .isInstanceOf(IllegalArgumentException.class);
    }

}


