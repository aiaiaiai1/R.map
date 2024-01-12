package rmap.exception.type;

import lombok.Getter;

@Getter
public enum GraphExceptionType implements ExceptionType {
    NOT_FOUND(
            0,
            "그래프가 존재 하지 않습니다.",
            "그래프가 존재 하지 않는 경우"
    )
    ;

    private final int errorCode;
    private final String message;
    private final String description;

    GraphExceptionType(int errorCode, String message, String description) {
        this.errorCode = errorCode;
        this.message = message;
        this.description = description;
    }
}
