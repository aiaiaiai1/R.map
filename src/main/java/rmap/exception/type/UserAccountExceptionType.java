package rmap.exception.type;

import lombok.Getter;

@Getter
public enum UserAccountExceptionType implements ExceptionType {
    NOT_FOUND(
            500,
            "계정이 존재 하지 않습니다.",
            "계정이 존재 하지 않는 경우"
    );

    private final int errorCode;
    private final String message;
    private final String description;

    UserAccountExceptionType(int errorCode, String message, String description) {
        this.errorCode = errorCode;
        this.message = message;
        this.description = description;
    }
}
