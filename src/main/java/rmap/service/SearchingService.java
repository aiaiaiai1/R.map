package rmap.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import rmap.entity.Notion;
import rmap.entity.NotionFolder;
import rmap.entity.SearchingType;
import rmap.repository.NotionFolderRepository;
import rmap.repository.NotionRepository;
import rmap.response.SearchingResponse;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchingService {

    private final NotionRepository notionRepository;
    private final NotionFolderRepository notionFolderRepository;

    public List<SearchingResponse> search(SearchingType searchingType, String keyword) {
        if (searchingType == SearchingType.NOTION) {
            return getNotionSearchingResponses(keyword);
        }

        if (searchingType == SearchingType.NOTION_FOLDER) {
            return getNotionFolderSearchingResponses(keyword);
        }

        throw new IllegalArgumentException();
    }

    private List<SearchingResponse> getNotionSearchingResponses(String keyword) {
        List<Notion> notions = notionRepository.findAllWithKeyword(keyword);
        return notions.stream()
                .map(n -> new SearchingResponse(n.getId(), n.getName()))
                .toList();
    }

    private List<SearchingResponse> getNotionFolderSearchingResponses(String keyword) {
        List<NotionFolder> notionFolders = notionFolderRepository.findAllWithKeyword(keyword);
        return notionFolders.stream()
                .map(nf -> new SearchingResponse(nf.getId(), nf.getName()))
                .toList();
    }

}
