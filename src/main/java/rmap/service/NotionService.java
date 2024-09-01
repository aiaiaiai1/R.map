package rmap.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rmap.entity.Edge;
import rmap.entity.Notion;
import rmap.entity.NotionFolder;
import rmap.entity.User;
import rmap.repository.EdgeRepository;
import rmap.repository.NotionFolderRepository;
import rmap.repository.NotionRepository;
import rmap.request.BuildNotionRequest;
import rmap.response.NotionIdResponse;
import rmap.response.NotionResponse;

@Service
@RequiredArgsConstructor
public class NotionService {

    private final NotionRepository notionRepository;
    private final EdgeRepository edgeRepository;
    private final NotionFolderRepository notionFolderRepository;

    public NotionResponse openNotion(User loginedUser, Long notionId) {
        Notion notion = notionRepository.findByIdOrThrow(notionId);
        NotionFolder notionFolder = notion.getNotionFolder();
        if (!notionFolder.isAccessibleBy(loginedUser)) {
            throw new IllegalArgumentException("비공개 노션입니다.");
        }
        return NotionResponse.from(notion);
    }

    public NotionIdResponse createInitialNotion(User loginedUser, BuildNotionRequest request) {
        NotionFolder notionFolder = notionFolderRepository.findByIdOrThrow(request.getNotionFolderId());
        Validator.validateNotionFolderOwner(notionFolder, loginedUser);
        Notion notion = new Notion(request.getName(), request.getContent(), notionFolder);
        return new NotionIdResponse(notionRepository.save(notion).getId());
    }

    @Transactional
    public void editNotion(User loginedUser, Long notionId, String notionName, String content) {
        Notion notion = notionRepository.findByIdOrThrow(notionId);
        Validator.validateNotionOwner(notion, loginedUser);
        notion.editName(notionName);
        notion.editContent(content);
    }

    @Transactional
    public void demolishNotion(User loginedUser, Long notionId) {
        Notion notion = notionRepository.findByIdOrThrow(notionId);
        Validator.validateNotionOwner(notion, loginedUser);
        List<Edge> edges = edgeRepository.findAllByNotionId(notion.getId());
        edgeRepository.deleteAllInBatch(edges);
        notionRepository.delete(notion);
    }
}
