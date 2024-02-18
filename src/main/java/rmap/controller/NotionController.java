package rmap.controller;

import jakarta.validation.Valid;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import rmap.request.BuildNotionRequest;
import rmap.request.EditNotionRequest;
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

//    @PatchMapping("/notion-relations/{id}")
//    public ResponseEntity<Void> editNotionRelations(
//            @PathVariable("id") Long notionId,
//            @RequestBody @Valid List<PatchRelatedNotionRequest> requests
//    ) {
//        notionFacade.editNotionRelations(notionId, requests);
//        return ResponseEntity.ok().build();
//    }
//
//    @DeleteMapping("/notion-relations")
//    public ResponseEntity<Void> disconnectNotionRelation(@RequestParam List<Long> notionIds) {
//        if (notionIds.size() != 2) {
//            throw new IllegalArgumentException("notion id's count is not 2");
//        }
//        notionFacade.disconnectNotionRelation(notionIds.get(0),notionIds.get(1));
//
//        return ResponseEntity.ok().build();
//    }
}
