package rmap.repository;

import rmap.entity.NotionFolder;
import rmap.entity.User;

import java.util.List;

public interface NotionFolderCustomRepository {

    List<NotionFolder> findAllWithKeyword(Long userId, String keyword);

}
