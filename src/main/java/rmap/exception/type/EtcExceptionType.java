package rmap.exception.type;

import lombok.Getter;

@Getter
public enum EtcExceptionType implements ExceptionType {
    ILLEGAL_SIZE(
            2000,
            "개수가 잘못 되었습니다",
            ""
    ),
    DUPLICATE_SIZE(
            2001,
            "개수가 중복되어 있습니다.",
            ""
    ),
    ;

    private final int errorCode;
    private final String message;
    private final String description;

    EtcExceptionType(int errorCode, String message, String description) {
        this.errorCode = errorCode;
        this.message = message;
        this.description = description;
    }
}
