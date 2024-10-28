package rmap.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import rmap.entity.NotionFolder;
import rmap.entity.User;

import java.util.List;

import static rmap.entity.QNotionFolder.notionFolder;

@RequiredArgsConstructor
public class NotionFolderCustomRepositoryImpl implements NotionFolderCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<NotionFolder> findAllWithKeyword(Long userId, String keyword) {
        return jpaQueryFactory.select(notionFolder)
                .from(notionFolder)
                .where(search(userId, keyword))
                .fetch();
    }

    private BooleanExpression search(Long userId, String keyword) {
        if (keyword == null) {
            return null;
        }
        return notionFolder.name.contains(keyword).and(notionFolder.owner.id.eq(userId).not());
    }
}
