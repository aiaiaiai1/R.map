package rmap.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import rmap.entity.User;
import rmap.global.Logined;
import rmap.response.OthersNotionFolderResponse;
import rmap.service.NotionFolderService;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final NotionFolderService notionFolderService;

    @GetMapping("/users/{userId}/notion-folders")
    public ResponseEntity<OthersNotionFolderResponse> readOthersNotionFolder(
            @Logined User user,
            @PathVariable("userId") Long targetUserId
    ) {
        OthersNotionFolderResponse response = notionFolderService.readOthersNotionFolders(targetUserId);
        return ResponseEntity.ok().body(response);
    }

}
