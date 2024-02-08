package rmap.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static rmap.EntityCreationSupporter.그래프_생성;
import static rmap.EntityCreationSupporter.노션_폴더_생성;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

class NotionFolderTest {

    @Test
    void 그래프가_존재하는지_확인_한다() {
        // given
        NotionFolder notionFolder = 노션_폴더_생성(1L, "폴더");
        List<Graph> graphs = new ArrayList<>();
        Graph graph = 그래프_생성(1L, notionFolder);
        graphs.add(graph);
        ReflectionTestUtils.setField(notionFolder, "graphs", graphs);

        // when, then
        assertThat(notionFolder.contains(graph)).isTrue();

    }

}
