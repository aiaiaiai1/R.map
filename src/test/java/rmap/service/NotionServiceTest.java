package rmap.service;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import rmap.entity.Notion;
import rmap.entity.NotionFolder;
import rmap.entity.User;
import rmap.repository.EdgeRepository;
import rmap.repository.NotionFolderRepository;
import rmap.repository.NotionRepository;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static rmap.EntityCreationSupporter.*;
import static rmap.Fixtures.*;

class NotionServiceTest extends ServiceTest {

    @Mock
    NotionFolderRepository notionFolderRepository;

    @Mock
    NotionRepository notionRepository;

    @Mock
    EdgeRepository edgeRepository;

    @InjectMocks
    NotionService notionService;


    @Nested
    class 노션_열람 {

        @Test
        void 비공개된_노션_폴더에_속한_노션_열람은_노션_폴더_소유자만_가능하고_소유자가_아닌_경우_예외가_발생한다() {
            // given
            User user = 유저_생성(1L, TEST_EMAIL, TEST_PASSWORD);
            User user1 = 유저_생성(2L, TEST_EMAIL_1, TEST_PASSWORD);

            NotionFolder notionFolder = 노션_폴더_생성(1L, user, "노션 폴더");
            notionFolder.setPrivateBy(user);

            Notion notion = 노션_생성(1L, "사과", "", notionFolder);

            given(notionRepository.findByIdOrThrow(notion.getId())).willReturn(notion);

            // when
            notionService.openNotion(user, notionFolder.getId());

            // then
            notionService.openNotion(user, notion.getId());
            assertThatThrownBy(() -> notionService.openNotion(user1, notion.getId()))
                    .hasMessage("비공개 노션입니다.");
        }

        @Test
        void 공개된_노션_폴더에_속한_노션_열람은_누구든지_가능하다() {
            // given
            User user = 유저_생성(1L, TEST_EMAIL, TEST_PASSWORD);
            User user1 = 유저_생성(2L, TEST_EMAIL_1, TEST_PASSWORD);

            NotionFolder notionFolder = 노션_폴더_생성(1L, user, "노션 폴더");

            Notion notion = 노션_생성(1L, "사과", "", notionFolder);

            given(notionRepository.findByIdOrThrow(notion.getId())).willReturn(notion);

            // when
            notionService.openNotion(user, notionFolder.getId());

            // then
            notionService.openNotion(user, notion.getId());
            notionService.openNotion(user1, notion.getId());
        }

    }
}
