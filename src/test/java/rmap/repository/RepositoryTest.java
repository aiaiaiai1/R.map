package rmap.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.FilterType;
import rmap.EntityPersistenceSupporter;

@DataJpaTest(
        includeFilters = @Filter(type = FilterType.ASSIGNABLE_TYPE, classes = EntityPersistenceSupporter.class)
)
public class RepositoryTest {

    @Autowired
    EntityPersistenceSupporter supporter;

}
