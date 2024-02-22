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
    NOT_IN_SAME_NOTION_FOLDER(
            102,
            "연결할 수 없는 위치에 속해 있습니다.",
            "서로 다른 노션 폴더에 있는 경우"
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
