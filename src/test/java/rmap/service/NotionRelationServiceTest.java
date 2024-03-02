package rmap.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static rmap.EntityCreationSupporter.노션_생성;
import static rmap.Fixtures.노션_폴더_알파벳;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import rmap.entity.Edge;
import rmap.entity.Notion;
import rmap.exception.InvalidAcessException;
import rmap.repository.EdgeRepository;
import rmap.repository.NotionFolderRepository;
import rmap.repository.NotionRepository;
import rmap.request.BuildNotionRequest;
import rmap.request.PatchRelatedNotionRequest;
import rmap.request.RelatedNotionInfo;
import rmap.response.NotionIdResponse;

class NotionRelationServiceTest extends ServiceTest {

    @Mock
    NotionRepository notionRepository;
    @Mock
    EdgeRepository edgeRepository;
    @Mock
    NotionFolderRepository notionFolderRepository;

    @InjectMocks
    NotionRelationService notionRelationService;

    @Nested
    class 하나의_노션에서_여러개의_연결관계_수정 {
        @Test
        void request에서_여러개의_노션_중_id_중복이_존재하는_경우_예외가_발생한다() {
            // given
            Notion notionA = 노션_생성(1L, "A", "", 노션_폴더_알파벳);

            List<PatchRelatedNotionRequest> requests = new ArrayList<>();
            requests.add(new PatchRelatedNotionRequest(1L, "", ""));
            requests.add(new PatchRelatedNotionRequest(1L, "", ""));

            given(notionRepository.findByIdOrThrow(notionA.getId())).willReturn(notionA);

            // when, then
            assertThatThrownBy(() -> notionRelationService.editNotionRelations(1L, requests))
                    .isInstanceOf(InvalidAcessException.class);
        }

        @Test
        void 하나의_노션에서_여러개의_연결관계를_수정한다() {
            // given
            Notion notionA = 노션_생성(1L, "A", "", 노션_폴더_알파벳);
            Notion notionB = 노션_생성(2L, "B", "", 노션_폴더_알파벳);
            Notion notionC = 노션_생성(3L, "C", "", 노션_폴더_알파벳);
            Notion notionD = 노션_생성(4L, "D", "", 노션_폴더_알파벳);
            Notion notionE = 노션_생성(5L, "E", "", 노션_폴더_알파벳);
             /*
                A - <B> - E
                    |    |
                    C  - D
             */
            connect(notionA, notionB);
            connect(notionC, notionD);
            connect(notionB, notionC);
            connect(notionB, notionE);
            connect(notionE, notionD);

            // B에 A 수정, E 삭제, C 그대로, D 추가
            List<PatchRelatedNotionRequest> requests = new ArrayList<>();
            requests.add(new PatchRelatedNotionRequest(notionA.getId(), "ba", "ab"));
            requests.add(new PatchRelatedNotionRequest(notionC.getId(), "", ""));
            requests.add(new PatchRelatedNotionRequest(notionD.getId(), "bd", "db"));

            given(notionRepository.findByIdOrThrow(notionA.getId())).willReturn(notionA);
            given(notionRepository.findByIdOrThrow(notionB.getId())).willReturn(notionB);
            given(notionRepository.findByIdOrThrow(notionC.getId())).willReturn(notionC);
            given(notionRepository.findByIdOrThrow(notionD.getId())).willReturn(notionD);
            given(notionRepository.findByIdOrThrow(notionE.getId())).willReturn(notionE);
            given(edgeRepository.save(any(Edge.class))).willReturn(null);

            // when
            notionRelationService.editNotionRelations(notionB.getId(), requests);

            // then
            then(edgeRepository).should(times(2)).save(any(Edge.class));
            assertThat(notionB.getEdges().stream()
                    .filter(edge -> edge.getTargetNotion().equals(notionE)).toList())
                    .hasSize(0);
            assertThat(notionB.getEdges().stream()
                    .filter(edge -> edge.getTargetNotion().equals(notionD)))
                    .hasSize(1);
            assertThat(notionB.getEdges().stream()
                    .filter(edge -> edge.getTargetNotion().equals(notionA)).findFirst().get()
                    .getDescription()).isEqualTo("ba");
            assertThat(notionB.getEdges().stream()
                    .filter(edge -> edge.getTargetNotion().equals(notionC)).findFirst().get()
                    .getDescription()).isEqualTo("");
        }

        private void connect(Notion notion1, Notion notion2) {
            notion1.connect(notion2, "");
            notion2.connect(notion1, "");
        }
    }

    @Test
    void 관계를_맺는_새로운_노션을_생셩한다() {
        // given

        Notion relatedNotion = 노션_생성(1L, "B", "b", 노션_폴더_알파벳);
        BuildNotionRequest request = new BuildNotionRequest(
                노션_폴더_알파벳.getId(),
                "A",
                "a",
                new RelatedNotionInfo(relatedNotion.getId(), "", "")
        );

        Notion newNotion = 노션_생성(2L, "A", "a", 노션_폴더_알파벳);

        given(notionFolderRepository.findByIdOrThrow(노션_폴더_알파벳.getId())).willReturn(노션_폴더_알파벳);
        given(notionRepository.findByIdOrThrow(relatedNotion.getId())).willReturn(relatedNotion);
        given(notionRepository.save(any(Notion.class))).willReturn(newNotion);
        given(edgeRepository.save(any(Edge.class))).willReturn(null);

        // when
        NotionIdResponse response = notionRelationService
                .createNotionConnectedWithRelatedNotion(request);

        // then
        assertThat(response.getId()).isEqualTo(newNotion.getId());
        then(edgeRepository).should(times(2)).save(any(Edge.class));
        assertThat(relatedNotion.getEdges()).hasSize(1);
        assertThat(newNotion.getEdges()).hasSize(1);
    }
}
