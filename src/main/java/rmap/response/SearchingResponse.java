package rmap.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class SearchingResponse {
    private final Long id;
    private final String name;
    private final String content;
    
}
