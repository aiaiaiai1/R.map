package rmap.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.times;
import static rmap.EntityCreationSupporter.노션_생성;
import static rmap.EntityCreationSupporter.노션_폴더_생성;
import static rmap.Fixtures.노션_폴더_알파벳;
import static rmap.Fixtures.노션_폴더_음식;

import java.util.List;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import rmap.entity.Notion;
import rmap.entity.NotionFolder;
import rmap.repository.NotionFolderRepository;
import rmap.repository.NotionRepository;
import rmap.response.NotionCompactResponse;
import rmap.response.NotionFolderResponse;

class NotionFolderServiceTest extends ServiceTest {
    @Mock
    NotionFolderRepository notionFolderRepository;

    @Mock
    NotionRepository notionRepository;

    @InjectMocks
    NotionFolderService notionFolderService;

    @Test
    void 노션_폴더_정보_조회시_노션은_사전순으로_조회한다() {
        // given
        Notion 노션_사과 = 노션_생성(1L, "사과", "", 노션_폴더_음식);
        Notion 노션_배 = 노션_생성(2L, "배", "", 노션_폴더_음식);

        given(notionFolderRepository.findByIdOrThrow(노션_폴더_음식.getId())).willReturn(노션_폴더_음식);
        given(notionRepository.findAllInNotionFolder(노션_폴더_음식.getId())).willReturn(List.of(노션_사과, 노션_배));

        // when
        NotionFolderResponse response = notionFolderService.readNotionFolderInfo(노션_폴더_음식.getId());

        // then
        List<NotionCompactResponse> results = response.getNotions();
        assertThat(results.get(0).getId()).isEqualTo(노션_배.getId());
        assertThat(results.get(1).getId()).isEqualTo(노션_사과.getId());
    }

    @Test
    void 기존의_노션_폴더_여러개를_새로운_노션_폴더_한개로_합친다() {
        // given
        Notion notion1 = 노션_생성(1L, "A", "a", 노션_폴더_알파벳);
        Notion notion2 = 노션_생성(2L, "사과", "과일", 노션_폴더_음식);

        NotionFolder newNotionFolder = 노션_폴더_생성(1L, "짬뽕");

        given(notionFolderRepository.save(any(NotionFolder.class))).willReturn(newNotionFolder);
        given(notionRepository.findAllInNotionFolder(노션_폴더_알파벳.getId())).willReturn(List.of(notion1));
        given(notionRepository.findAllInNotionFolder(노션_폴더_음식.getId())).willReturn(List.of(notion2));
        willDoNothing().given(notionFolderRepository).deleteById(any(Long.class));

        // when
        notionFolderService.mergeNotionFolderWithNew(
                "new",
                List.of(노션_폴더_음식.getId(), 노션_폴더_알파벳.getId())
        );

        // then
        assertThat(notion1.getNotionFolder()).isEqualTo(newNotionFolder);
        assertThat(notion2.getNotionFolder()).isEqualTo(newNotionFolder);
        then(notionFolderRepository).should(times(2)).deleteById(any(Long.class));
    }

    @Nested
    class 노션_폴더_분리 {

        @Test
        void 하나의_그래프를_새로운_노션_폴더로_분리한다() {
            // given
            Notion notionA = 노션_생성(1L, "A", "a", 노션_폴더_알파벳);
            Notion notionB = 노션_생성(2L, "B", "과일", 노션_폴더_알파벳);
            Notion notionC = 노션_생성(3L, "C", "과일", 노션_폴더_알파벳);

            NotionFolder newNotionFolder = 노션_폴더_생성(1L, "new");

            notionB.connect(notionC, "");
            notionC.connect(notionB, "");

            given(notionFolderRepository.save(any(NotionFolder.class))).willReturn(newNotionFolder);
            given(notionFolderRepository.findByIdOrThrow(노션_폴더_알파벳.getId())).willReturn(노션_폴더_알파벳);
            given(notionRepository.findByIdOrThrow(notionB.getId())).willReturn(notionB);

            // when
            notionFolderService.splitNotionFolderWithNew(newNotionFolder.getName(), 노션_폴더_알파벳.getId(), notionB.getId());

            // then
            assertThat(notionA.getNotionFolder()).isEqualTo(노션_폴더_알파벳);
            assertThat(notionB.getNotionFolder()).isEqualTo(newNotionFolder);
            assertThat(notionC.getNotionFolder()).isEqualTo(newNotionFolder);
        }

        @Test
        void 노션폴더에_속하지_않는_그래프인_경우_예외가_발생한다() {
            // given
            Notion notionA = 노션_생성(1L, "A", "a", 노션_폴더_알파벳);
            Notion notionB = 노션_생성(2L, "B", "과일", 노션_폴더_알파벳);
            Notion notionC = 노션_생성(3L, "C", "과일", 노션_폴더_알파벳);

            NotionFolder newNotionFolder = 노션_폴더_생성(1L, "new");

            notionB.connect(notionC, "");
            notionC.connect(notionB, "");

            given(notionFolderRepository.findByIdOrThrow(노션_폴더_음식.getId())).willReturn(노션_폴더_음식);
            given(notionRepository.findByIdOrThrow(notionB.getId())).willReturn(notionB);

            // when, then
            assertThatThrownBy(
                    () -> notionFolderService.splitNotionFolderWithNew(newNotionFolder.getName(), 노션_폴더_음식.getId(),
                            notionB.getId())
            ).isInstanceOf(IllegalArgumentException.class);
        }
    }


}
