package rmap.repository;

import rmap.entity.Notion;

import java.util.List;

public interface NotionCustomRepository {

    List<Notion> findAllWithKeyword(String keyword);
}
