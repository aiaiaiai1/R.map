package rmap.service;

import java.util.Comparator;
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
import rmap.response.GraphResponse;
import rmap.response.NotionFolderResponse;

@Service
@RequiredArgsConstructor
public class NotionFolderService {

    private final NotionFolderRepository notionFolderRepository;
    private final NotionRepository notionRepository;
    private final EdgeRepository edgeRepository;

    public List<NotionFolder> readAllNotionFolders() {
        return notionFolderRepository.findAll();
    }

    public NotionFolder createNotionFolder(String name) {
        NotionFolder notionFolder = new NotionFolder(name);
        return notionFolderRepository.save(notionFolder);
    }

    @Transactional
    public void deleteNotionFolder(Long notionFolderId) {
        NotionFolder notionFolder = notionFolderRepository.findByIdOrThrow(notionFolderId);
        List<Notion> notions = notionRepository.findAllInNotionFolder(notionFolderId);
        for (Notion notion : notions) {
            List<Edge> edges = edgeRepository.findAllByNotionId(notion.getId());
            edgeRepository.deleteAllInBatch(edges);
            notionRepository.delete(notion);
        }
        notionFolderRepository.delete(notionFolder);
        // 중복 생김 , 도메인에서 처리 할려 했으나 양뱡향에 걸려버림. 중복 제거 방법은??
    }

    public NotionFolder readNotionFolder(Long notionFolderId) {
        return notionFolderRepository.findByIdOrThrow(notionFolderId);
    }

    public NotionFolderResponse readNotionFolderInfo(Long notionFolderId) {
        NotionFolder notionFolder = readNotionFolder(notionFolderId);
        List<Notion> notions = notionRepository.findAllInNotionFolder(notionFolder.getId());
        List<Notion> sortedNotions = notions.stream()
                .sorted(Comparator.comparing(Notion::getName))
                .toList();
        return NotionFolderResponse.of(notionFolder, sortedNotions);
    }

    public List<GraphResponse> readAllGraphs(Long notionFolderId) {
        NotionFolder notionFolder = notionFolderRepository.findByIdOrThrow(notionFolderId);
        List<Notion> notions = notionRepository.findAllInNotionFolder(notionFolder.getId());
        List<List<Notion>> graphs = NotionSearcher.convertToGraphs(notions);
        return graphs.stream()
                .map(graph -> GraphResponse.of(graph))
                .toList();
    }

    @Transactional
    public void mergeNotionFolderWithNew(String notionFolderName, List<Long> notionFolderIds) {
        NotionFolder newNotionFolder = createNotionFolder(notionFolderName);
        changeNotionFoldersTo(newNotionFolder, notionFolderIds);
    }

    private void changeNotionFoldersTo(NotionFolder notionFolder, List<Long> notionFolderIds) {
        for (Long id : notionFolderIds) {
            List<Notion> notions = notionRepository.findAllInNotionFolder(id);
            notions.stream()
                    .forEach(n -> n.changeNotionFolder(notionFolder));
            notionFolderRepository.deleteById(id);
        }
    }

}
