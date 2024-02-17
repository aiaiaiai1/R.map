package rmap.service;

import static org.assertj.core.api.Assertions.assertThat;
import static rmap.EntityCreationSupporter.그래프_생성;
import static rmap.EntityCreationSupporter.노션_생성;
import static rmap.Fixtures.노션_폴더_음식;

import java.util.List;
import org.junit.jupiter.api.Test;
import rmap.entity.Edge;
import rmap.entity.Graph;
import rmap.entity.Notion;

class NotionSearcherTest {

    @Test
    void 깊이_우선_탐색으로_탐색한_노션_리스트를_확인한다() {
        // given
        Graph graph = 그래프_생성(1L, 노션_폴더_음식);
        Notion notionA = 노션_생성(1L, "A", "과일", graph);
        Notion notionB = 노션_생성(2L, "B", "과일", graph);
        Notion notionC = 노션_생성(3L, "C", "과일", graph);
        Notion notionD = 노션_생성(4L, "D", "과일", graph);
        Notion notionE = 노션_생성(5L, "E", "과일", graph);
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
        new Edge(notion1, notion2, "");
        new Edge(notion2, notion1, "");
    }

}
