package rmap.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rmap.request.BuildNotionRequest;
import rmap.request.EditNotionRequest;
import rmap.request.PatchRelatedNotionRequest;
import rmap.response.NotionIdResponse;
import rmap.response.NotionResponse;
import rmap.service.NotionFacade;
import rmap.service.NotionService;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class NotionController {

    private final NotionFacade notionFacade;
    private final NotionService notionService;

    @PostMapping("/notions")
    public ResponseEntity<NotionIdResponse> buildNotion(@RequestBody @Valid BuildNotionRequest request) {
        NotionIdResponse response = notionFacade.buildNotion(request);
        return ResponseEntity.created(URI.create("/notions" + response.getId())).body(response);
    }

    @GetMapping("/notions/{id}")
    public ResponseEntity<NotionResponse> readNotion(@PathVariable("id") Long notionId) {
        NotionResponse response = notionFacade.readNotion(notionId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/notions/{id}")
    public ResponseEntity<Void> demolishNotion(@PathVariable("id") Long notionId) {
        notionFacade.demolishNotion(notionId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/notions/{id}")
    public ResponseEntity<Void> editNotion(
            @PathVariable("id") Long notionId,
            @RequestBody @Valid EditNotionRequest request
    ) {
        notionFacade.editNotion(notionId, request.getName(), request.getContent());
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/notion-relations/{id}")
    public ResponseEntity<Void> editNotionRelations(
            @PathVariable("id") Long notionId,
            @RequestBody @Valid List<PatchRelatedNotionRequest> requests
    ) {
        notionFacade.editNotionRelations(notionId, requests);
        return ResponseEntity.ok().build();
    }

}
