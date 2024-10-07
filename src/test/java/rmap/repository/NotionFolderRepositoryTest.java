package rmap.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import rmap.Fixtures;
import rmap.entity.NotionFolder;
import rmap.entity.User;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class NotionFolderRepositoryTest extends RepositoryTest {

    @Autowired
    NotionFolderRepository notionFolderRepository;

    @Test
    void 검색어와_일치하는_모든_노션_폴더를_찾는다() {
        // given
        User user = supporter.유저_저장(Fixtures.TEST_EMAIL, Fixtures.TEST_PASSWORD);
        User user1 = supporter.유저_저장(Fixtures.TEST_EMAIL, Fixtures.TEST_PASSWORD);
        NotionFolder notionFolder = supporter.노션_폴더_저장(user, "알파벳");
        NotionFolder notionFolder1 = supporter.노션_폴더_저장(user1, "알파벳");

        // when
        List<NotionFolder> notionFolders = notionFolderRepository.findAllWithKeyword(-1L, "알파벳");

        // then
        assertThat(notionFolders).hasSize(2);
    }
}
