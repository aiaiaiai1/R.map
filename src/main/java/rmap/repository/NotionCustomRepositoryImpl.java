package rmap.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import rmap.entity.Notion;

import java.util.List;

import static rmap.entity.QNotion.notion;

@RequiredArgsConstructor
public class NotionCustomRepositoryImpl implements NotionCustomRepository {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Notion> findAllWithKeyword(Long userId, String keyword) {
        return jpaQueryFactory.select(notion)
                .from(notion)
                .where(search(userId, keyword))
                .fetch();
    }

    private BooleanExpression search(Long userId, String keyword) {
        if (keyword == null) {
            return null;
        }
        return (notion.name.contains(keyword).or(notion.content.contains(keyword))).and(notion.notionFolder.owner.id.eq(userId).not());
    }

}
