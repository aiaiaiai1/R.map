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
    public List<Notion> findAllWithKeyword(String keyword) {
        return jpaQueryFactory.select(notion)
                .from(notion)
                .where(search(keyword))
                .fetch();
    }

    private BooleanExpression search(String keyword) {
        if (keyword == null) {
            return null;
        }
        return notion.name.like(keyword);
    }

}
