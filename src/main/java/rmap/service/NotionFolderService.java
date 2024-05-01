package rmap.service;

import java.util.Comparator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rmap.entity.Edge;
import rmap.entity.Notion;
import rmap.entity.NotionFolder;
import rmap.entity.User;
import rmap.exception.BusinessRuleException;
import rmap.exception.DataConsistencyException;
import rmap.exception.type.DataConsistencyExceptionType;
import rmap.exception.type.NotionFolderExceptionType;
import rmap.repository.EdgeRepository;
import rmap.repository.NotionFolderRepository;
import rmap.repository.NotionRepository;
import rmap.response.GraphResponse;
import rmap.response.OpenNotionFolderResponse;

@Service
@RequiredArgsConstructor
public class NotionFolderService {

    private final NotionFolderRepository notionFolderRepository;
    private final NotionRepository notionRepository;
    private final EdgeRepository edgeRepository;

    public List<NotionFolder> readAllNotionFoldersOf(User loginedUser) {
        return notionFolderRepository.findAllOf(loginedUser.getId());
    }

    public NotionFolder createNotionFolder(User loginedUser, String notionFolderName) {
        NotionFolder notionFolder = new NotionFolder(loginedUser, notionFolderName);
        return notionFolderRepository.save(notionFolder);
    }

    @Transactional
    public void splitNotionFolderWithNew(
            User loginedUser, String newNotionFolderName, Long notionFolderId, Long targetNotionId
    ) {
        NotionFolder notionFolder = notionFolderRepository.findByIdOrThrow(notionFolderId);
        validateNotionFolderOwner(notionFolder, loginedUser);

        Notion notion = notionRepository.findByIdOrThrow(targetNotionId);
        if (!notion.getNotionFolder().equals(notionFolder)) {
            throw new DataConsistencyException(DataConsistencyExceptionType.NOT_MATCH_NOTION_FOLDER_AND_NOTION);
        }

        List<Notion> graph = NotionSearcher.searchDepthFirst(notion);
        // 사이즈 ? 검사 대상으로 어떤걸 사용할까
        if (graph.size() == notionRepository.findAllInNotionFolder(notionFolderId).size()) {
            throw new BusinessRuleException(NotionFolderExceptionType.CAN_NOT_SPLIT);
        }

        NotionFolder newNotionFolder = createNotionFolder(loginedUser, newNotionFolderName);
        for (Notion n : graph) {
            n.changeNotionFolder(newNotionFolder);
        }
    }

    @Transactional
    public void mergeNotionFolderWithNew(
            User loginedUser, String newNotionFolderName, List<Long> targetNotionFolderIds
    ) {
        NotionFolder newNotionFolder = createNotionFolder(loginedUser, newNotionFolderName);
        changeNotionFoldersToNew(loginedUser, newNotionFolder, targetNotionFolderIds);
    }

    private void changeNotionFoldersToNew(
            User loginedUser, NotionFolder newNotionFolder, List<Long> targetNotionFolderIds
    ) {
        for (Long id : targetNotionFolderIds) {
            NotionFolder notionFolder = notionFolderRepository.findByIdOrThrow(id);
            validateNotionFolderOwner(notionFolder, loginedUser);
            List<Notion> notions = notionRepository.findAllInNotionFolder(id);
            notions.stream()
                    .forEach(n -> n.changeNotionFolder(newNotionFolder));
            notionFolderRepository.deleteById(id);
        }
    }

    @Transactional
    public void deleteNotionFolder(User loginedUser, Long notionFolderId) {
        NotionFolder notionFolder = notionFolderRepository.findByIdOrThrow(notionFolderId);
        validateNotionFolderOwner(notionFolder, loginedUser);
        List<Notion> notions = notionRepository.findAllInNotionFolder(notionFolderId);
        for (Notion notion : notions) {
            List<Edge> edges = edgeRepository.findAllByNotionId(notion.getId());
            edgeRepository.deleteAllInBatch(edges);
            notionRepository.delete(notion);
        }
        notionFolderRepository.delete(notionFolder);
        // 중복 생김 , 도메인에서 처리 할려 했으나 양뱡향에 걸려버림. 중복 제거 방법은??
    }

    public OpenNotionFolderResponse openNotionFolder(User loginedUser, Long notionFolderId) {
        NotionFolder notionFolder = notionFolderRepository.findByIdOrThrow(notionFolderId);
        validateNotionFolderOwner(notionFolder, loginedUser);
        List<Notion> notions = notionRepository.findAllInNotionFolder(notionFolder.getId());
        List<Notion> sortedNotions = notions.stream()
                .sorted(Comparator.comparing(Notion::getName))
                .toList();
        return OpenNotionFolderResponse.of(notionFolder, sortedNotions);
    }

    public List<GraphResponse> readAllGraphs(Long notionFolderId) {
        NotionFolder notionFolder = notionFolderRepository.findByIdOrThrow(notionFolderId);
        List<Notion> notions = notionRepository.findAllInNotionFolder(notionFolder.getId());
        List<List<Notion>> graphs = NotionSearcher.convertToGraphs(notions);
        return graphs.stream()
                .map(graph -> GraphResponse.of(graph))
                .toList();
    }

    private void validateNotionFolderOwner(NotionFolder notionFolder, User loginedUser) {
        if (!notionFolder.isOwner(loginedUser)) {
            throw new IllegalArgumentException("권한이 없습니다.");
        }
    }

    public void editNotionFolderName(Long notionFolderId, String name) {
        NotionFolder notionFolder = notionFolderRepository.findByIdOrThrow(notionFolderId);
        notionFolder.changeName(name);
    }
}
