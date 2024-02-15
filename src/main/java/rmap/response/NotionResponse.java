package rmap.response;

import java.util.List;
import java.util.NoSuchElementException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import rmap.entity.Notion;

@Getter
@RequiredArgsConstructor
public class NotionResponse {

    private final Long id;
    private final String name;
    private final String content;
    private final NotionFolderCompactResponse notionFolder;
    private final List<RelatedNotionResponse> relatedNotions;

    public static NotionResponse from(Notion notion) {
        List<RelatedNotionResponse> responses = notion.getEdges().stream()
                .map(edge -> {
                    Notion targetNotion = edge.getTargetNotion();
                    return new RelatedNotionResponse(
                            targetNotion.getId(),
                            targetNotion.getName(),
                            edge.getDescription(),
                            targetNotion.getEdges().stream()
                                    .filter(e -> e.getTargetNotion().equals(notion))
                                    .findAny().orElseThrow(()-> new NoSuchElementException("No value present - edge"))
                                    .getDescription());
                })
                .toList();
        // n+1 문제 해결해야할듯

        return new NotionResponse(
                notion.getId(),
                notion.getName(),
                notion.getContent(),
                new NotionFolderCompactResponse(notion.getGraph().getNotionFolder()),
                responses
        );
    }

}
