package rmap.exception.type;

import lombok.Getter;

@Getter
public enum EdgeExceptionType implements ExceptionType {
    SELF_LOOP(
            100,
            "동일한 연결 지점 입니다.",
            "소스 노션과 타겟 노션이 같은 경우"
    ),
    SELF_DISCONNECTION(
            101,
            "잘못된 끊기 요청입니다.",
            "소스 노션과 타겟 노션이 같은 경우"
    ),
    ;

    private final int errorCode;
    private final String message;
    private final String description;

    EdgeExceptionType(int errorCode, String message, String description) {
        this.errorCode = errorCode;
        this.message = message;
        this.description = description;
    }
}
