package rmap.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;
import static rmap.EntityCreationSupporter.노션_생성;
import static rmap.EntityCreationSupporter.엣지_생성;
import static rmap.Fixtures.노션_폴더_음식;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.util.ReflectionTestUtils;
import rmap.entity.Edge;
import rmap.entity.Notion;
import rmap.repository.EdgeRepository;

class EdgeServiceTest extends ServiceTest {

//    @Mock
//    EdgeRepository edgeRepository;
//
//    @InjectMocks
//    EdgeService edgeService;
//
//    @Test
//    void 두개의_노션을_연결한다() {
//        // given
//        Notion notionA = 노션_생성(1L, "사과", "", 노션_폴더_음식);
//        Notion notionB = 노션_생성(2L, "배", "", 노션_폴더_음식);
//
//        Edge edge = new Edge(notionA, notionB, "");
//        ReflectionTestUtils.setField(edge, "id", 1L);
//        given(edgeRepository.save(any(Edge.class))).willReturn(edge);
//
//        assertThat(notionA.getEdges()).hasSize(0);
//
//        // when
//        edgeService.connect(notionA, notionB, "");
//
//        // then
//        assertThat(notionA.getEdges()).hasSize(1);
//    }
//
//    @Test
//    void 두개의_노션을_끊는다() {
//        // given
//        Notion notionA = 노션_생성(1L, "사과", "", 노션_폴더_음식);
//        Notion notionB = 노션_생성(2L, "배", "", 노션_폴더_음식);
//
//        Edge edge = 엣지_생성(1L, notionA, notionB, "");
//        assertThat(notionA.getEdges()).hasSize(1);
//
//        given(edgeRepository.findByNotionIds(notionA.getId(), notionB.getId())).willReturn(edge);
//        willDoNothing().given(edgeRepository).delete(edge);
//
//        // when
//        edgeService.disconnect(notionA, notionB);
//
//        // then
//        assertThat(notionA.getEdges()).hasSize(0);
//        then(edgeRepository).should().delete(any());
//    }

}
