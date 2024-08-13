package rmap.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import rmap.Fixtures;
import rmap.entity.Notion;
import rmap.entity.NotionFolder;
import rmap.entity.User;

class NotionRepositoryTest extends RepositoryTest {

    @Autowired
    NotionRepository notionRepository;

    @Test
    void 노션_폴더에_존재하는_모든_노션을_조회한다() {
        // given
        User user = supporter.유저_저장(Fixtures.TEST_EMAIL, Fixtures.TEST_PASSWORD);
        NotionFolder notionFolder = supporter.노션_폴더_저장(user, "알파벳");
        Notion notionA = supporter.노션_저장("A", "", notionFolder);
        Notion notionB = supporter.노션_저장("B", "", notionFolder);
        Notion notionC = supporter.노션_저장("C", "", notionFolder);

        // when
        List<Notion> notions = notionRepository.findAllInNotionFolder(notionFolder.getId());

        // then
        assertThat(notions).hasSize(3);
    }
}
