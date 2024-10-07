package rmap.repository;

import rmap.entity.Notion;
import rmap.entity.User;

import java.util.List;

public interface NotionCustomRepository {

    List<Notion> findAllWithKeyword(Long userId, String keyword);
}
