package rmap.exception.type;

import lombok.Getter;

@Getter
public enum NotionExceptionType implements ExceptionType {
    NOT_FOUND(
            300,
            "노션이 존재하지 않습니다.",
            "노션이 존재하지 않는 경우"
    );

    private final int errorCode;
    private final String message;
    private final String description;

    NotionExceptionType(int errorCode, String message, String description) {
        this.errorCode = errorCode;
        this.message = message;
        this.description = description;
    }
}
