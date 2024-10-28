package rmap.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import rmap.entity.User;
import rmap.global.Logined;
import rmap.response.SearchNotionFolderResponse;
import rmap.response.SearchNotionsResponse;
import rmap.service.SearchingService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class SearchingController {

    private final SearchingService searchingService;

    @GetMapping("/search-list/notion")
    public ResponseEntity<List<SearchNotionsResponse>> searchNotions(
            @Logined User user,
            @RequestParam("keyword") String keyword
    ) {
        List<SearchNotionsResponse> responses = searchingService.searchNotions(user, keyword);

        return ResponseEntity.ok().body(responses);
    }

    @GetMapping("/search-list/notion-folder")
    public ResponseEntity<List<SearchNotionFolderResponse>> searchNotionFolder(
            @Logined User user,
            @RequestParam("keyword") String keyword
    ) {
        List<SearchNotionFolderResponse> reponses = searchingService.searchNotionFolder(user, keyword);
        return ResponseEntity.ok().body(reponses);
    }
}
