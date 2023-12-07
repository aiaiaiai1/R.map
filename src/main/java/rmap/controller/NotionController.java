package rmap.controller;

import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import rmap.request.BuildNotionRequest;
import rmap.response.NotionIdResponse;
import rmap.response.NotionResponse;
import rmap.service.NotionFacade;

@RestController
@RequiredArgsConstructor
public class NotionController {

    private final NotionFacade notionFacade;

    @PostMapping("/notions")
    public ResponseEntity<NotionIdResponse> buildNotion(@RequestBody BuildNotionRequest request) {
        NotionIdResponse response = notionFacade.buildNotion(request);
        return ResponseEntity.created(URI.create("/notions" + response.getId())).body(response);
    }

    @GetMapping("/notions/{id}")
    public ResponseEntity<NotionResponse> readNotion(@PathVariable("id") Long notionId) {
        NotionResponse response = notionFacade.readNotion(notionId);
        return ResponseEntity.ok(response);
    }

}
