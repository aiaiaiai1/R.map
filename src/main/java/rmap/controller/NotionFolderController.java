package rmap.controller;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rmap.entity.NotionFolder;
import rmap.request.NotionFolderRequest;
import rmap.response.IdResponse;
import rmap.response.NotionFolderCompactResponse;
import rmap.response.NotionFolderResponse;
import rmap.service.NotionFolderService;

@RestController
@RequestMapping("/notion-folders")
@RequiredArgsConstructor
public class NotionFolderController {

    private final NotionFolderService notionFolderService;

    @GetMapping
    public ResponseEntity<List<NotionFolderCompactResponse>> readNotionFolders() {
        List<NotionFolder> notionFolders = notionFolderService.readAll();
        List<NotionFolderCompactResponse> responses = notionFolders.stream()
                .map(NotionFolderCompactResponse::new)
                .toList();
        return ResponseEntity.ok(responses);
    }

    @PostMapping
    public ResponseEntity<IdResponse> createNotionFolder(@RequestBody @Valid NotionFolderRequest request) {
        NotionFolder notionFolder = notionFolderService.createNotionFolder(request.getName());
        return ResponseEntity.created(URI.create("/notionFolders/" + notionFolder.getId()))
                .body(new IdResponse(notionFolder.getId()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<NotionFolderResponse> readNotionFolder(
            @PathVariable("id") Long notionFolderId
    ) {
        NotionFolderResponse responses = notionFolderService.readNotionFolderInfo(notionFolderId);
        return ResponseEntity.ok(responses);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNotionFolder(@PathVariable("id") Long notionFolderId) {
        notionFolderService.deleteNotionFolder(notionFolderId);
        return ResponseEntity.ok().build();
    }
}
