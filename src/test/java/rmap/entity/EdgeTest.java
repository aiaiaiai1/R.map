package rmap.entity;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static rmap.EntityCreationSupporter.노션_생성;
import static rmap.EntityCreationSupporter.노션_폴더_생성;

import org.junit.jupiter.api.Test;
import rmap.exception.BusinessRuleException;

class EdgeTest {

    @Test
    void 같은_노션을_연결하는_경우_예외가_발생한다() {
        // given
        NotionFolder notionFolder = 노션_폴더_생성(1L, "폴더");

        Notion notion = 노션_생성(1L, "개념", "내용", notionFolder);
        Notion notion1 = 노션_생성(1L, "개념", "내용", notionFolder);

        // when, then
        assertThatThrownBy(() -> new Edge(notion, notion1, ""))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessage("동일한 연결 지점 입니다.");
    }

    @Test
    void 노션의_id가_null_인_경우_예외가_발생한다() {
        // given
        NotionFolder notionFolder = 노션_폴더_생성(1L, "폴더");

        Notion notion = 노션_생성(null, "개념", "내용", notionFolder);
        Notion notion1 = 노션_생성(1L, "개념", "내용", notionFolder);

        // when, then
        assertThatThrownBy(() -> new Edge(notion, notion1, ""))
                .isInstanceOf(IllegalArgumentException.class);

    }

    @Test
    void 노션이_null_인_경우_예외가_발생한다() {
        // given
        NotionFolder notionFolder = 노션_폴더_생성(1L, "폴더");

        Notion notion = 노션_생성(1L, "개념", "내용", notionFolder);
        Notion notion1 = null;

        // when, then
        assertThatThrownBy(() -> new Edge(notion, notion1, ""))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 서로_다른_노션_폴더인_경우_예외가_발생한다() {
        // given
        NotionFolder notionFolder1 = 노션_폴더_생성(1L, "폴더");
        NotionFolder notionFolder2 = 노션_폴더_생성(2L, "폴더");

        Notion notion1 = 노션_생성(1L, "개념", "내용", notionFolder1);
        Notion notion2 = 노션_생성(2L, "개념", "내용", notionFolder2);

        // when, then
        assertThatThrownBy(() -> new Edge(notion1, notion2, ""))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessage("연결할 수 없는 위치에 속해 있습니다.");
    }

}


