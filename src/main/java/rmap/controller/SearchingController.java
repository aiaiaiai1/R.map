package rmap.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import rmap.entity.SearchingType;
import rmap.response.SearchingResponse;
import rmap.service.SearchingService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class SearchingController {

    private final SearchingService searchingService;

    @GetMapping("/search-list")
    public ResponseEntity<List<SearchingResponse>> search(
            @RequestParam("type") SearchingType searchingType,
            @RequestParam("keyword") String keyword
    ) {
        List<SearchingResponse> responses = searchingService.search(searchingType, keyword);
        return ResponseEntity.ok().body(responses);
    }
}
