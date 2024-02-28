package rmap.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static rmap.EntityCreationSupporter.노션_생성;
import static rmap.Fixtures.노션_폴더_음식;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import rmap.entity.Notion;
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

}
