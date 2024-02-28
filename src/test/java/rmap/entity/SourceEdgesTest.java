package rmap.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static rmap.EntityCreationSupporter.노션_생성;
import static rmap.EntityCreationSupporter.노션_폴더_생성;

import java.util.List;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class SourceEdgesTest {

    @Nested
    class 노션_연결 {
        @Test
        void 두개의_노션을_연결한다() {
            // given
            NotionFolder notionFolder = 노션_폴더_생성(1L, "폴더");
            Notion notion1 = 노션_생성(1L, "개념", "내용", notionFolder);
            Notion notion2 = 노션_생성(2L, "개념", "내용", notionFolder);

            SourceEdges sourceEdges = new SourceEdges();

            // when
            sourceEdges.addNewEdge(notion1, notion2, "");

            // then
            assertThat(sourceEdges.getEdges()).hasSize(1);
        }

        @Test
        void 이미_연결된_노션_인_경우_예외가_발생한다_() {
            // given
            NotionFolder notionFolder = 노션_폴더_생성(1L, "폴더");
            Notion notion1 = 노션_생성(1L, "개념", "내용", notionFolder);
            Notion notion2 = 노션_생성(2L, "개념", "내용", notionFolder);

            SourceEdges sourceEdges = new SourceEdges(List.of(new Edge(notion1, notion2, "")));
            // when, then

            assertThatThrownBy(() -> sourceEdges.addNewEdge(notion1, notion2, ""))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Test
    void 노션_연결을_삭제한다() {
        // given
        NotionFolder notionFolder = 노션_폴더_생성(1L, "폴더");
        Notion notion1 = 노션_생성(1L, "개념", "내용", notionFolder);
        Notion notion2 = 노션_생성(2L, "개념", "내용", notionFolder);

        SourceEdges sourceEdges = new SourceEdges(List.of(new Edge(notion1, notion2, "")));
        assertThat(sourceEdges.getEdges()).hasSize(1);

        // when
        sourceEdges.removeEdge(notion2);

        // then
        assertThat(sourceEdges.getEdges()).hasSize(0);
    }

    @Nested
    class 노션_연결_내용_수정 {
        @Test
        void 노션_연결_내용을_수정한다() {
            // given
            NotionFolder notionFolder = 노션_폴더_생성(1L, "폴더");
            Notion notion1 = 노션_생성(1L, "개념", "내용", notionFolder);
            Notion notion2 = 노션_생성(2L, "개념", "내용", notionFolder);

            SourceEdges sourceEdges = new SourceEdges(List.of(new Edge(notion1, notion2, "")));

            // when
            sourceEdges.editDescription(notion2, "변경됨");

            // then
            assertThat(sourceEdges.getEdges().get(0).getDescription()).isEqualTo("변경됨");
        }

        @Test
        void 노션_연결이_존재하지_않는_경우_예외가_발생한다() {
            // given
            NotionFolder notionFolder = 노션_폴더_생성(1L, "폴더");
            Notion notion1 = 노션_생성(1L, "개념", "내용", notionFolder);
            Notion notion2 = 노션_생성(2L, "개념", "내용", notionFolder);
            Notion notion3 = 노션_생성(3L, "개념", "내용", notionFolder);

            SourceEdges sourceEdges = new SourceEdges(List.of(new Edge(notion1, notion2, "")));

            // when, then
            assertThatThrownBy(() -> sourceEdges.editDescription(notion3, "변경됨"))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

}
