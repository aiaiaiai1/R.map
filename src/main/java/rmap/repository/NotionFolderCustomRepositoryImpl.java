package rmap.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import rmap.entity.NotionFolder;

import java.util.List;

import static rmap.entity.QNotionFolder.notionFolder;

@RequiredArgsConstructor
public class NotionFolderCustomRepositoryImpl implements NotionFolderCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<NotionFolder> findAllWithKeyword(String keyword) {
        return jpaQueryFactory.select(notionFolder)
                .from(notionFolder)
                .where(search(keyword))
                .fetch();
    }

    private BooleanExpression search(String keyword) {
        if (keyword == null) {
            return null;
        }
        return notionFolder.name.like(keyword);
    }
}
