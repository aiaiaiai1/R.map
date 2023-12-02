package rmap.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

class NotionTest {

    @Nested
    class 노션_연결 {
        @Test
        void 노션에_노션을_연결_할_수_있다() {
            // given
            Notion notion = new Notion("개념", "내용");
            Notion notion1 = new Notion("개념1", "내용1");
            ReflectionTestUtils.setField(notion, "id", 1L);
            ReflectionTestUtils.setField(notion1, "id", 2L);

            // when
            Edge edge = notion.connectTo(notion1, "개념->개념1");

            // then
            assertThat(edge.getSourceNotion()).isEqualTo(notion);
            assertThat(edge.getTargetNotion()).isEqualTo(notion1);
            assertThat(edge.getDescription()).isEqualTo("개념->개념1");
        }

        @Test
        void 자기_자신과_연결하는_경우_예외가_발생한다() {
            // given
            Notion notion = new Notion("개념", "내용");

            // when, then
            assertThatThrownBy(() -> notion.connectTo(notion, null))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 아이디가_존재하지_않는_경우_예외가_발생한다() {
            // given
            Notion notion = new Notion("개념", "내용");
            Notion notion1 = new Notion("개념", "내용");

            // when, then
            assertThatThrownBy(() -> notion.connectTo(notion1, null))
                    .isInstanceOf(IllegalArgumentException.class);

        }

        @Test
        void 노션이_null_인_경우_예외가_발생한다() {
            // given
            Notion notion = new Notion("개념", "내용");
            Notion notion1 = null;

            // when, then
            assertThatThrownBy(() -> notion.connectTo(notion1, null))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 이미_연결된_노션인_경우_예외가_발생한다() {
            // given
            Notion notion = new Notion("개념", "내용");
            Notion notion1 = new Notion("개념1", "내용1");
            ReflectionTestUtils.setField(notion, "id", 1L);
            ReflectionTestUtils.setField(notion1, "id", 2L);
            notion.connectTo(notion1, null);

            // when, then
            assertThatThrownBy(() -> notion.connectTo(notion1, null))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
