package rmap.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.times;
import static rmap.EntityCreationSupporter.노션_생성;
import static rmap.EntityCreationSupporter.엣지_생성;
import static rmap.Fixtures.노션_폴더_알파벳;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import rmap.entity.Notion;
import rmap.exception.InvalidAcessException;
import rmap.request.PatchRelatedNotionRequest;

class NotionFacadeTest extends ServiceTest {

    @Mock
    NotionService notionService;
    @Mock
    NotionFolderService notionFolderService;
    @Mock
    EdgeService edgeService;

    @InjectMocks
    NotionFacade notionFacade;


    @Nested
    class 하나의_노션에서_여러개의_연결관계_수정 {

        static long autoIncrementEdgeId = 1;

        @Test
        void request에서_여러개의_노션_중_id_중복이_존재하는_경우_예외가_발생한다() {
            // given
            Notion notionA = 노션_생성(1L, "A", "", 노션_폴더_알파벳);

            List<PatchRelatedNotionRequest> requests = new ArrayList<>();
            requests.add(new PatchRelatedNotionRequest(1L, "", ""));
            requests.add(new PatchRelatedNotionRequest(1L, "", ""));

            given(notionService.readNotion(notionA.getId())).willReturn(notionA);

            // when, then
            assertThatThrownBy(() -> notionFacade.editNotionRelations(1L, requests))
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

            given(notionService.readNotion(notionA.getId())).willReturn(notionA);
            given(notionService.readNotion(notionB.getId())).willReturn(notionB);
            given(notionService.readNotion(notionC.getId())).willReturn(notionC);
            given(notionService.readNotion(notionD.getId())).willReturn(notionD);
            given(notionService.readNotion(notionE.getId())).willReturn(notionE);
            given(edgeService.connect(any(Notion.class), any(Notion.class), any(String.class))).willReturn(null);
            willDoNothing().given(edgeService).disconnect(any(Notion.class), any(Notion.class));

            // when
            notionFacade.editNotionRelations(notionB.getId(), requests);

            // then
            then(edgeService).should().editDescription(notionB, notionA, "ba");
            then(edgeService).should().editDescription(notionA, notionB, "ab");

            then(edgeService).should().disconnect(notionB, notionE);
            then(edgeService).should().disconnect(notionE, notionB);
            then(edgeService).should(times(2)).disconnect(any(Notion.class), any(Notion.class));

            then(edgeService).should().editDescription(notionB, notionC, "");
            then(edgeService).should().editDescription(notionC, notionB, "");
            then(edgeService).should(times(4)).editDescription(any(Notion.class), any(Notion.class), any(String.class));

            then(edgeService).should().connect(notionB, notionD, "bd");
            then(edgeService).should().connect(notionD, notionB, "db");
            then(edgeService).should(times(2)).connect(any(Notion.class), any(Notion.class), any(String.class));
        }

        private void connect(Notion notion1, Notion notion2) {
            엣지_생성(autoIncrementEdgeId++, notion1, notion2, "");
            엣지_생성(autoIncrementEdgeId++, notion2, notion1, "");
        }
    }
}
