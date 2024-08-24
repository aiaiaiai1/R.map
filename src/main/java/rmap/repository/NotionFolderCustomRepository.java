package rmap.repository;

import rmap.entity.NotionFolder;

import java.util.List;

public interface NotionFolderCustomRepository {

    List<NotionFolder> findAllWithKeyword(String keyword);

}
