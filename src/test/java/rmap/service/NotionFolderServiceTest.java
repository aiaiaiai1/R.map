package rmap.service;

import static java.util.List.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.times;
import static rmap.EntityCreationSupporter.노션_생성;
import static rmap.EntityCreationSupporter.노션_폴더_생성;
import static rmap.EntityCreationSupporter.유저_생성;
import static rmap.Fixtures.TEST_EMAIL;
import static rmap.Fixtures.TEST_PASSWORD;
import static rmap.Fixtures.알맵이;
import static rmap.Fixtures.알맵이의_노션_폴더_알파벳;
import static rmap.Fixtures.알맵이의_노션_폴더_음식;

import java.util.List;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import rmap.entity.Notion;
import rmap.entity.NotionFolder;
import rmap.entity.User;
import rmap.exception.DataConsistencyException;
import rmap.repository.NotionFolderRepository;
import rmap.repository.NotionRepository;
import rmap.response.NotionCompactResponse;
import rmap.response.OpenNotionFolderResponse;

class NotionFolderServiceTest extends ServiceTest {
    @Mock
    NotionFolderRepository notionFolderRepository;

    @Mock
    NotionRepository notionRepository;

    @InjectMocks
    NotionFolderService notionFolderService;


    @Nested
    class 노션_폴더_열람 {
        @Test
        void 노션_폴더_열람시_노션은_사전순으로_조회한다() {
            // given
            NotionFolder notionFolder = 알맵이의_노션_폴더_음식;
            User user = notionFolder.getOwner();

            Notion 노션_사과 = 노션_생성(1L, "사과", "", notionFolder);
            Notion 노션_배 = 노션_생성(2L, "배", "", notionFolder);

            given(notionFolderRepository.findByIdOrThrow(notionFolder.getId())).willReturn(notionFolder);
            given(notionRepository.findAllInNotionFolder(notionFolder.getId())).willReturn(of(노션_사과, 노션_배));

            // when
            OpenNotionFolderResponse response = notionFolderService.openNotionFolder(user, notionFolder.getId());

            // then
            List<NotionCompactResponse> results = response.getNotions();
            assertThat(results.get(0).getId()).isEqualTo(노션_배.getId());
            assertThat(results.get(1).getId()).isEqualTo(노션_사과.getId());
        }

        @Test
        void 노션_폴더_소유자가_아닌_경우_예외가_발생_한다() {
            NotionFolder notionFolder = 알맵이의_노션_폴더_음식;
            User user = 유저_생성(1L, TEST_EMAIL, TEST_PASSWORD);

            Notion 노션_사과 = 노션_생성(1L, "사과", "", notionFolder);
            Notion 노션_배 = 노션_생성(2L, "배", "", notionFolder);

            given(notionFolderRepository.findByIdOrThrow(notionFolder.getId())).willReturn(notionFolder);

            // when, then
            assertThatThrownBy(() -> notionFolderService.openNotionFolder(user, notionFolder.getId()))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("권한이 없습니다.");
        }

    }

    @Test
    void 노션_폴더_삭제시_소유자가_아닌_경우_예외가_발생한다() {
        // given
        User user = 유저_생성(1L, TEST_EMAIL, TEST_PASSWORD);
        NotionFolder notionfolder = 알맵이의_노션_폴더_음식;
        given(notionFolderRepository.findByIdOrThrow(notionfolder.getId())).willReturn(notionfolder);

        // when, then
        assertThatThrownBy(() -> notionFolderService.deleteNotionFolder(user, notionfolder.getId()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("권한이 없습니다.");
    }

    @Nested
    class 노션_합치기 {
        @Test
        void 기존의_노션_폴더_여러개를_새로운_노션_폴더_한개로_합친다() {
            // given
            Notion notion1 = 노션_생성(1L, "A", "a", 알맵이의_노션_폴더_알파벳);
            Notion notion2 = 노션_생성(2L, "사과", "과일", 알맵이의_노션_폴더_음식);

            NotionFolder newNotionFolder = 노션_폴더_생성(1L, 알맵이, "짬뽕");

            given(notionFolderRepository.save(any(NotionFolder.class))).willReturn(newNotionFolder);
            given(notionFolderRepository.findByIdOrThrow(알맵이의_노션_폴더_알파벳.getId())).willReturn(알맵이의_노션_폴더_알파벳);
            given(notionFolderRepository.findByIdOrThrow(알맵이의_노션_폴더_음식.getId())).willReturn(알맵이의_노션_폴더_음식);
            given(notionRepository.findAllInNotionFolder(알맵이의_노션_폴더_알파벳.getId())).willReturn(of(notion1));
            given(notionRepository.findAllInNotionFolder(알맵이의_노션_폴더_음식.getId())).willReturn(of(notion2));
            willDoNothing().given(notionFolderRepository).deleteById(any(Long.class));

            // when
            notionFolderService.mergeNotionFolderWithNew(
                    알맵이,
                    "new",
                    of(알맵이의_노션_폴더_음식.getId(), 알맵이의_노션_폴더_알파벳.getId())
            );

            // then
            assertThat(notion1.getNotionFolder()).isEqualTo(newNotionFolder);
            assertThat(notion2.getNotionFolder()).isEqualTo(newNotionFolder);
            then(notionFolderRepository).should(times(2)).deleteById(any(Long.class));
        }

        @Test
        void 노션_폴더의_소유자가_아닌_경우_예외가_발생한다() {
            Notion notion1 = 노션_생성(1L, "A", "a", 알맵이의_노션_폴더_알파벳);

            User user = 유저_생성(1L, TEST_EMAIL, TEST_PASSWORD);
            NotionFolder notionFolder = 노션_폴더_생성(1L, user, "짬뽕");
            Notion notion2 = 노션_생성(2L, "사과", "과일", notionFolder);

            NotionFolder newNotionFolder = 노션_폴더_생성(2L, 알맵이, "new");

            given(notionFolderRepository.save(any(NotionFolder.class))).willReturn(newNotionFolder);
            given(notionFolderRepository.findByIdOrThrow(알맵이의_노션_폴더_알파벳.getId())).willReturn(알맵이의_노션_폴더_알파벳);
            given(notionFolderRepository.findByIdOrThrow(notionFolder.getId())).willReturn(notionFolder);

            // when, then
            assertThatThrownBy(() -> notionFolderService.mergeNotionFolderWithNew(
                    알맵이,
                    "new",
                    List.of(알맵이의_노션_폴더_알파벳.getId(), notionFolder.getId())))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("권한이 없습니다.");
        }
    }

    @Nested
    class 노션_폴더_분리 {

        @Test
        void 하나의_그래프를_새로운_노션_폴더로_분리한다() {
            // given
            Notion notionA = 노션_생성(1L, "A", "a", 알맵이의_노션_폴더_알파벳);
            Notion notionB = 노션_생성(2L, "B", "과일", 알맵이의_노션_폴더_알파벳);
            Notion notionC = 노션_생성(3L, "C", "과일", 알맵이의_노션_폴더_알파벳);

            User user = 알맵이의_노션_폴더_알파벳.getOwner();

            NotionFolder newNotionFolder = 노션_폴더_생성(1L, user, "new");

            notionB.connect(notionC, "");
            notionC.connect(notionB, "");

            given(notionFolderRepository.save(any(NotionFolder.class))).willReturn(newNotionFolder);
            given(notionFolderRepository.findByIdOrThrow(알맵이의_노션_폴더_알파벳.getId())).willReturn(알맵이의_노션_폴더_알파벳);
            given(notionRepository.findByIdOrThrow(notionB.getId())).willReturn(notionB);

            // when
            notionFolderService.splitNotionFolderWithNew(user, newNotionFolder.getName(), 알맵이의_노션_폴더_알파벳.getId(),
                    notionB.getId());

            // then
            assertThat(notionA.getNotionFolder()).isEqualTo(알맵이의_노션_폴더_알파벳);
            assertThat(notionB.getNotionFolder()).isEqualTo(newNotionFolder);
            assertThat(notionC.getNotionFolder()).isEqualTo(newNotionFolder);
        }

        @Test
        void 노션_폴더의_소유자가_아닌_경우_예외가_발생한다() {
            // given
            Notion notionA = 노션_생성(1L, "A", "a", 알맵이의_노션_폴더_알파벳);
            Notion notionB = 노션_생성(2L, "B", "과일", 알맵이의_노션_폴더_알파벳);
            Notion notionC = 노션_생성(3L, "C", "과일", 알맵이의_노션_폴더_알파벳);

            User user = 유저_생성(1L, TEST_EMAIL, TEST_PASSWORD);

            NotionFolder newNotionFolder = 노션_폴더_생성(1L, user, "new");

            notionB.connect(notionC, "");
            notionC.connect(notionB, "");

            given(notionFolderRepository.findByIdOrThrow(알맵이의_노션_폴더_알파벳.getId())).willReturn(알맵이의_노션_폴더_알파벳);

            // when, then
            assertThatThrownBy(() -> notionFolderService.splitNotionFolderWithNew(user, newNotionFolder.getName(),
                    알맵이의_노션_폴더_알파벳.getId(), notionB.getId()))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("권한이 없습니다.");

        }

        @Test
        void 노션_폴더에_속하지_않는_그래프인_경우_예외가_발생한다() {
            // given
            NotionFolder notionFolder1 = 알맵이의_노션_폴더_알파벳;
            Notion notionA = 노션_생성(1L, "A", "a", notionFolder1);
            Notion notionB = 노션_생성(2L, "B", "과일", notionFolder1);
            Notion notionC = 노션_생성(3L, "C", "과일", notionFolder1);

            User user = notionFolder1.getOwner();

            NotionFolder newNotionFolder = 노션_폴더_생성(1L, user, "new");

            notionB.connect(notionC, "");
            notionC.connect(notionB, "");

            NotionFolder notionFolder2 = 알맵이의_노션_폴더_음식;
            given(notionFolderRepository.findByIdOrThrow(notionFolder2.getId())).willReturn(notionFolder2);
            given(notionRepository.findByIdOrThrow(notionB.getId())).willReturn(notionB);

            // when, then
            assertThatThrownBy(
                    () -> notionFolderService.splitNotionFolderWithNew(
                            user, newNotionFolder.getName(), notionFolder2.getId(), notionB.getId()
                    )
            ).isInstanceOf(DataConsistencyException.class);
        }

        @Test
        void 노션_폴더_수정시_소유자가_아닌_경우_예외가_발생한다() {
            // given
            NotionFolder notionFolder = 알맵이의_노션_폴더_음식;
            User user = 유저_생성(1L, TEST_EMAIL, TEST_PASSWORD);

            given(notionFolderRepository.findByIdOrThrow(notionFolder.getId())).willReturn(notionFolder);

            // when, then
            assertThatThrownBy(() -> notionFolderService.editNotionFolderName(user, notionFolder.getId(), "new"))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("권한이 없습니다.");
        }

        @Test
        void 노션_폴더의_그래프_조회시_소유자가_아닌_경우_예외가_발생한다() {
            // given
            NotionFolder notionFolder = 알맵이의_노션_폴더_음식;
            User user = 유저_생성(1L, TEST_EMAIL, TEST_PASSWORD);

            given(notionFolderRepository.findByIdOrThrow(notionFolder.getId())).willReturn(notionFolder);

            // when, then
            assertThatThrownBy(() -> notionFolderService.readAllGraphsIn(user, notionFolder.getId()))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("권한이 없습니다.");
        }

    }


}
