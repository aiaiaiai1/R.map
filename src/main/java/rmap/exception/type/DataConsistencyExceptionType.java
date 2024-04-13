package rmap.exception.type;

import lombok.Getter;

@Getter
public enum DataConsistencyExceptionType implements ExceptionType {
    NOT_MATCH_NOTION_FOLDER_AND_NOTION(
            1000,
            "노션 폴더와 노션간의 연결관계가 일치하지 않습니다.",
            "노션 폴더와 노션간의 연결관계가 일치하지 않는 경우"
    ),
    ;

    private final int errorCode;
    private final String message;
    private final String description;

    DataConsistencyExceptionType(int errorCode, String message, String description) {
        this.errorCode = errorCode;
        this.message = message;
        this.description = description;
    }
}
