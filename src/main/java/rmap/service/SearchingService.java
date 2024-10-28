package rmap.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import rmap.entity.Notion;
import rmap.entity.NotionFolder;
import rmap.entity.User;
import rmap.repository.NotionFolderRepository;
import rmap.repository.NotionRepository;
import rmap.response.SearchNotionFolderResponse;
import rmap.response.SearchNotionsResponse;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchingService {

    private final NotionRepository notionRepository;
    private final NotionFolderRepository notionFolderRepository;

    public List<SearchNotionsResponse> searchNotions(User user, String keyword) {
        List<Notion> notions = notionRepository.findAllWithKeyword(user.getId(), keyword);
        return notions.stream()
                .map(SearchNotionsResponse::new)
                .toList();
    }

    public List<SearchNotionFolderResponse> searchNotionFolder(User user, String keyword) {
        List<NotionFolder> notionFolders = notionFolderRepository.findAllWithKeyword(user.getId(), keyword);
        List<SearchNotionFolderResponse> response = new ArrayList<>();

        for (NotionFolder notionFolder : notionFolders) {
            List<Notion> notions = notionRepository.findAllInNotionFolder(notionFolder.getId());
            response.add(new SearchNotionFolderResponse(notionFolder, notions));
        }
        return response;
    }

}
