package rmap.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static rmap.EntityCreationSupporter.그래프_생성;
import static rmap.EntityCreationSupporter.노션_생성;
import static rmap.Fixtures.노션_폴더_음식;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import rmap.entity.Graph;
import rmap.entity.Notion;
import rmap.repository.GraphRepository;

@ExtendWith(MockitoExtension.class)
class GraphServiceTest {

    @Mock
    GraphRepository graphRepository;

    @InjectMocks
    GraphService graphService;

    @Test
    void 그래프에_속한_노션들을_오름차순으로_조회한다() {
        // given
        Graph graph = 그래프_생성(1L, 노션_폴더_음식);
        Notion 노션_사과 = 노션_생성(1L, "사과", "", graph);
        Notion 노션_배 = 노션_생성(2L, "배", "", graph);
        ReflectionTestUtils.setField(graph, "notions", List.of(노션_사과, 노션_배));

        given(graphRepository.findByIdOrThrow(graph.getId())).willReturn(graph);

        // when
        List<Notion> results = graphService.readNotionsOfGraph(graph.getId());

        // then
        assertThat(results.get(0)).isEqualTo(노션_배);
        assertThat(results.get(1)).isEqualTo(노션_사과);
    }

}
