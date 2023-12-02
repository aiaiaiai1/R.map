package rmap.entity;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class NotionEdgeTest {

    @Autowired
    EntityManager entityManager;

    @Test
    void s() {
        // given
        Notion notion = new Notion("1", null);
        ReflectionTestUtils.setField(notion, "id", 1L);
        Notion notion1 = new Notion("1", null);
        Edge edge = new Edge(notion, notion1, null);
        entityManager.persist(edge);

        entityManager.flush();
        entityManager.clear();
        // when

        // then

    }

}
