package rmap;

import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Component;
import rmap.entity.Edge;
import rmap.entity.Notion;
import rmap.entity.NotionFolder;

@Component
public class EntityPersistenceSupporter {

    private final EntityManager entityManager;

    public EntityPersistenceSupporter(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public NotionFolder 노션_폴더_저장(String name) {
        NotionFolder notionFolder = new NotionFolder(name);
        entityManager.persist(notionFolder);
        return notionFolder;
    }

    public Notion 노션_저장(String name, String content, NotionFolder notionFolder) {
        Notion notion = new Notion(name, content, notionFolder);
        entityManager.persist(notion);
        return notion;
    }

    public Edge 엣지_저장(Notion soucreNotion, Notion targetNotion, String description) {
        Edge edge = new Edge(soucreNotion, targetNotion, description);
        entityManager.persist(edge);
        return edge;
    }

}
