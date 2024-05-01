package rmap.controller;

import jakarta.validation.Valid;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import rmap.entity.User;
import rmap.exception.EtcException;
import rmap.exception.type.EtcExceptionType;
import rmap.global.Logined;
import rmap.request.BuildNotionRequest;
import rmap.request.EditNotionRequest;
import rmap.request.PatchRelatedNotionRequest;
import rmap.response.NotionIdResponse;
import rmap.response.NotionResponse;
import rmap.service.NotionRelationService;
import rmap.service.NotionService;

@RestController
@RequiredArgsConstructor
public class NotionController {

    private final NotionRelationService notionRelationService;
    private final NotionService notionService;

    @PostMapping("/notions")
    public ResponseEntity<NotionIdResponse> buildNotion(
            @Logined User user,
            @RequestBody @Valid BuildNotionRequest request
    ) {
        NotionIdResponse response = getCreatedNotionIdResponse(user, request);
        return ResponseEntity.created(URI.create("/notions" + response.getId())).body(response);
    }

    private NotionIdResponse getCreatedNotionIdResponse(User loginedUser, BuildNotionRequest request) {
        if (isInitialNotionRequest(request)) {
            return notionService.createInitialNotion(loginedUser, request);
        }
        return notionRelationService.createNotionConnectedWithRelatedNotion(loginedUser, request);
    }

    private boolean isInitialNotionRequest(BuildNotionRequest request) {
        return request.getRelatedNotion() == null;
    }

    @GetMapping("/notions/{id}")
    public ResponseEntity<NotionResponse> readNotion(
            @Logined User user,
            @PathVariable("id") Long notionId
    ) {
        NotionResponse response = notionService.openNotion(user, notionId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/notions/{id}")
    public ResponseEntity<Void> demolishNotion(
            @Logined User user,
            @PathVariable("id") Long notionId
    ) {
        notionService.demolishNotion(user, notionId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/notions/{id}")
    public ResponseEntity<Void> editNotion(
            @Logined User user,
            @PathVariable("id") Long notionId,
            @RequestBody @Valid EditNotionRequest request
    ) {
        notionService.editNotion(user, notionId, request.getName(), request.getContent());
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/notion-relations/{id}")
    public ResponseEntity<Void> editNotionRelations(
            @Logined User user,
            @PathVariable("id") Long notionId,
            @RequestBody @Valid List<PatchRelatedNotionRequest> requests
    ) {
        notionRelationService.editNotionRelations(user, notionId, requests);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/notion-relations")
    public ResponseEntity<Void> disconnectNotionRelation(
            @Logined User user,
            @RequestParam List<Long> notionIds
    ) {
        if (notionIds.size() != 2) {
            throw new EtcException(EtcExceptionType.ILLEGAL_SIZE);
        }
        notionRelationService.disconnectNotionRelation(user, notionIds.get(0), notionIds.get(1));

        return ResponseEntity.ok().build();
    }
}
