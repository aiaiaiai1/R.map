package rmap.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import rmap.entity.Edge;
import rmap.entity.Notion;
import rmap.entity.NotionFolder;

class EdgeRepositoryTest extends RepositoryTest {

    @Autowired
    EdgeRepository edgeRepository;

    private NotionFolder notionFolder;

    @BeforeEach
    void initData() {
        notionFolder = supporter.노션_폴더_저장("알파벳");
    }

    @Test
    void 노션이_가지고있는_모든_출발과_도착_엣지를_조회한다() {
        // given
        Notion notionA = supporter.노션_저장("A", "", notionFolder);
        Notion notionB = supporter.노션_저장("B", "", notionFolder);
        Notion notionC = supporter.노션_저장("C", "", notionFolder);

        supporter.엣지_저장(notionA, notionB, "");
        supporter.엣지_저장(notionB, notionA, "");
        supporter.엣지_저장(notionA, notionC, "");
        supporter.엣지_저장(notionB, notionC, "");

        // when
        List<Edge> results = edgeRepository.findAllByNotionId(notionA.getId());

        // then
        assertThat(results).hasSize(3);
    }

}
