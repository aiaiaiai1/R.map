package rmap.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rmap.entity.Edge;
import rmap.entity.Notion;
import rmap.entity.NotionFolder;
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

    public NotionResponse readNotion(Long notionId) {
        Notion notion = notionRepository.findByIdOrThrow(notionId);
        return NotionResponse.from(notion);
    }

    public NotionIdResponse createInitialNotion(BuildNotionRequest request) {
        NotionFolder notionFolder = notionFolderRepository.findByIdOrThrow(request.getNotionFolderId());
        Notion notion = new Notion(request.getName(), request.getContent(), notionFolder);
        return new NotionIdResponse(notionRepository.save(notion).getId());
    }

    @Transactional
    public void demolishNotion(Long notionId) {
        Notion notion = notionRepository.findByIdOrThrow(notionId);
        List<Edge> edges = edgeRepository.findAllByNotionId(notion.getId());
        edgeRepository.deleteAllInBatch(edges);
        notionRepository.delete(notion);
    }

    @Transactional
    public void editNotion(Long notionId, String name, String content) {
        Notion notion = notionRepository.findByIdOrThrow(notionId);
        notion.editName(name);
        notion.editContent(content);
    }
}
