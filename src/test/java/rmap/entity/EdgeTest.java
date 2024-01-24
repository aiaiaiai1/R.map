package rmap.entity;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static rmap.entity.EntityCreationSupporter.그래프_생성;
import static rmap.entity.EntityCreationSupporter.노션_생성;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import rmap.exception.BusinessRuleException;

@SpringBootTest
@Transactional
class EdgeTest {

    @Test
    void 같은_노션을_연결하는_경우_예외가_발생한다() {
        // given
        Graph graph = 그래프_생성(1L, "그래프");
        Notion notion = 노션_생성(1L, "개념", "내용", graph);
        Notion notion1 = 노션_생성(1L, "개념", "내용", graph);

        // when, then
        assertThatThrownBy(() -> new Edge(notion, notion1))
                .isInstanceOf(BusinessRuleException.class);
    }

    @Test
    void 노션의_아이디가_존재하지_않는_경우_예외가_발생한다() {
        // given
        Graph graph = 그래프_생성(1L, "그래프");
        Notion notion = 노션_생성(null, "개념", "내용", graph);
        Notion notion1 = 노션_생성(1L, "개념", "내용", graph);

        // when, then
        assertThatThrownBy(() -> new Edge(notion, notion1))
                .isInstanceOf(IllegalArgumentException.class);

    }

    @Test
    void 노션이_null_인_경우_예외가_발생한다() {
        // given
        Graph graph = 그래프_생성(1L, "그래프");
        Notion notion = 노션_생성(1L, "개념", "내용", graph);
        Notion notion1 = null;

        // when, then
        assertThatThrownBy(() -> new Edge(notion, notion1))
                .isInstanceOf(IllegalArgumentException.class);
    }

}


