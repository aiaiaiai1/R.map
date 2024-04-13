package rmap.exception.type;

import lombok.Getter;

@Getter
public enum VerificationExceptionType implements ExceptionType {
    ;

    private final int errorCode;
    private final String message;
    private final String description;

    VerificationExceptionType(int errorCode, String message, String description) {
        this.errorCode = errorCode;
        this.message = message;
        this.description = description;
    }
}
