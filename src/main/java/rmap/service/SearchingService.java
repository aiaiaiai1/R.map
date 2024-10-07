package rmap.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import rmap.entity.Notion;
import rmap.entity.NotionFolder;
import rmap.entity.SearchingType;
import rmap.entity.User;
import rmap.repository.NotionFolderRepository;
import rmap.repository.NotionRepository;
import rmap.response.SearchingResponse;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchingService {

    private final NotionRepository notionRepository;
    private final NotionFolderRepository notionFolderRepository;

    public List<SearchingResponse> search(User user, SearchingType searchingType, String keyword) {
        if (searchingType == SearchingType.NOTION) {
            return getNotionSearchingResponses(user, keyword);
        }

        if (searchingType == SearchingType.NOTION_FOLDER) {
            return getNotionFolderSearchingResponses(user, keyword);
        }

        throw new IllegalArgumentException();
    }

    private List<SearchingResponse> getNotionSearchingResponses(User user, String keyword) {
        List<Notion> notions = notionRepository.findAllWithKeyword(user.getId(), keyword);
        return notions.stream()
                .map(n -> new SearchingResponse(n.getId(), n.getName()))
                .toList();
    }

    private List<SearchingResponse> getNotionFolderSearchingResponses(User user, String keyword) {
        List<NotionFolder> notionFolders = notionFolderRepository.findAllWithKeyword(user.getId(), keyword);
        return notionFolders.stream()
                .map(nf -> new SearchingResponse(nf.getId(), nf.getName()))
                .toList();
    }

}
