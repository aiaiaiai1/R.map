package rmap.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static rmap.entity.EntityCreationSupporter.그래프_생성;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

class NotionTest {

    @Nested
    class 엣지_연결 {

        @Test
        void 노션에_엣지를_연결_할_수_있다() {
            // given
            Graph graph = 그래프_생성(1L, "개발");
            Notion notion = new Notion("개념", "내용", graph);
            Notion notion1 = new Notion("개념1", "내용1", graph);
            ReflectionTestUtils.setField(notion, "id", 1L);
            ReflectionTestUtils.setField(notion1, "id", 2L);

            Edge edge = new Edge(notion, notion1);

            // when
            notion.connect(edge);

            // then
            assertThat(notion.getEdges()).contains(edge);
        }

        @Test
        void 노션이_엣지의_출발_노션과_일치하지_않는_경우_예외가_발생_한다() {
            // given
            Graph graph = 그래프_생성(1L, "개발");
            Notion notion = new Notion("개념", "내용", graph);
            Notion notion1 = new Notion("개념1", "내용1", graph);
            ReflectionTestUtils.setField(notion, "id", 1L);
            ReflectionTestUtils.setField(notion1, "id", 2L);

            Edge edge = new Edge(notion, notion1);

            // when, then
            assertThatThrownBy(() -> notion1.connect(edge))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 노션에_같은_엣지가_존재하는_경우_예외가_발생_한다() {
            // given
            Graph graph = 그래프_생성(1L, "개발");
            Notion notion = new Notion("개념", "내용", graph);
            Notion notion1 = new Notion("개념1", "내용1", graph);
            ReflectionTestUtils.setField(notion, "id", 1L);
            ReflectionTestUtils.setField(notion1, "id", 2L);

            Edge edge = new Edge(notion, notion1);
            notion.connect(edge);

            // when, then
            assertThatThrownBy(() -> notion.connect(edge))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

}
