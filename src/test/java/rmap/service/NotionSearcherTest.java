package rmap.service;

import static org.assertj.core.api.Assertions.assertThat;
import static rmap.EntityCreationSupporter.노션_생성;
import static rmap.Fixtures.노션_폴더_음식;

import java.util.List;
import org.junit.jupiter.api.Test;
import rmap.entity.Notion;

class NotionSearcherTest {

    @Test
    void 깊이_우선_탐색으로_탐색한_노션을_확인한다() {
        // given
        Notion notionA = 노션_생성(1L, "A", "과일", 노션_폴더_음식);
        Notion notionB = 노션_생성(2L, "B", "과일", 노션_폴더_음식);
        Notion notionC = 노션_생성(3L, "C", "과일", 노션_폴더_음식);
        Notion notionD = 노션_생성(4L, "D", "과일", 노션_폴더_음식);
        Notion notionE = 노션_생성(5L, "E", "과일", 노션_폴더_음식);
         /*
              A - B - E
                  |   |
                  C - D
         */
        connect(notionA, notionB);
        connect(notionC, notionD);
        connect(notionB, notionC);
        connect(notionB, notionE);
        connect(notionE, notionD);

        // when
        List<Notion> results = NotionSearcher.searchDepthFirst(notionB);

        // then
        assertThat(results).hasSize(5);
    }

    private void connect(Notion notion1, Notion notion2) {
        notion1.connect(notion2, "");
        notion2.connect(notion1, "");
    }

    @Test
    void 여러_노션을_그래프별로_분리한다() {
        // given
        Notion notionA = 노션_생성(1L, "A", "과일", 노션_폴더_음식);
        Notion notionB = 노션_생성(2L, "B", "과일", 노션_폴더_음식);
        Notion notionC = 노션_생성(3L, "C", "과일", 노션_폴더_음식);
        Notion notionD = 노션_생성(4L, "D", "과일", 노션_폴더_음식);
        Notion notionE = 노션_생성(5L, "E", "과일", 노션_폴더_음식);
         /*
              A - B   E
                  |   |
                  C   D
         */
        connect(notionA, notionB);
        connect(notionB, notionC);
        connect(notionE, notionD);

        // when
        List<List<Notion>> graphs = NotionSearcher.convertToGraphs(
                List.of(notionA, notionB, notionC, notionD, notionE));

        // then
        assertThat(graphs).hasSize(2);
        assertThat(graphs.get(0)).containsExactlyInAnyOrder(notionA, notionB, notionC);
        assertThat(graphs.get(1)).containsExactlyInAnyOrder(notionD, notionE);
    }
}
