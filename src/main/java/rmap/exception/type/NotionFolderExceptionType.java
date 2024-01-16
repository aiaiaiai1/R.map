package rmap.exception.type;

import lombok.Getter;

@Getter
public enum NotionFolderExceptionType implements ExceptionType {
    NOT_FOUND(
            400,
            "노션 폴더가 존재하지 않습니다.",
            "노션폴더가 존재하지 않는 경우"
    );

    private final int errorCode;
    private final String message;
    private final String description;

    NotionFolderExceptionType(int errorCode, String message, String description) {
        this.errorCode = errorCode;
        this.message = message;
        this.description = description;
    }
}
