package rmap.controller;

import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import rmap.entity.Notion;
import rmap.request.BuildNotionRequest;
import rmap.request.EditNotionRequest;
import rmap.response.NotionCompactResponse;
import rmap.response.NotionIdResponse;
import rmap.response.NotionResponse;
import rmap.service.NotionFacade;
import rmap.service.NotionService;

@RestController
@RequiredArgsConstructor
public class NotionController {

    private final NotionFacade notionFacade;
    private final NotionService notionService;

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

    @GetMapping("/notion-folders/{id}/notions")
    public ResponseEntity<List<NotionCompactResponse>> readNotionsInNotionFolder(
            @PathVariable("id") Long notionFolderId
    ) {
        List<Notion> notions = notionService.readAllInNotionFolder(notionFolderId);
        List<NotionCompactResponse> responses = notions.stream()
                .map(NotionCompactResponse::new)
                .toList();
        return ResponseEntity.ok(responses);
    }

    @DeleteMapping("/notions/{id}")
    public ResponseEntity<Void> demolishNotion(@PathVariable("id") Long notionId) {
        notionFacade.demolishNotion(notionId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/notions/{id}")
    public ResponseEntity<Void> editNotion(
            @PathVariable("id") Long notionId,
            @RequestBody EditNotionRequest request
    ) {
        notionFacade.editNotion(notionId, request.getName(), request.getContent());
        return ResponseEntity.ok().build();
    }

}
