package rmap.entity;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class EdgeTest {

    @Test
    void 같은_노션을_연결하는_경우_예외가_발생한다() {
        // given
        Notion notion = new Notion("개념", "내용");
        ReflectionTestUtils.setField(notion, "id", 1L);
        Notion notion1 = new Notion("개념", "내용");
        ReflectionTestUtils.setField(notion1, "id", 1L);

        // when, then
        assertThatThrownBy(() -> new Edge(notion, notion1, null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 노션의_아이디가_존재하지_않는_경우_예외가_발생한다() {
        // given
        Notion notion = new Notion("개념", "내용");
        Notion notion1 = new Notion("개념", "내용");

        // when, then
        assertThatThrownBy(() -> new Edge(notion, notion1, null))
                .isInstanceOf(IllegalArgumentException.class);

    }

    @Test
    void 노션이_null_인_경우_예외가_발생한다() {
        // given
        Notion notion = new Notion("개념", "내용");
        Notion notion1 = null;

        // when, then
        assertThatThrownBy(() -> new Edge(notion, notion1, null))
                .isInstanceOf(IllegalArgumentException.class);
    }

}


