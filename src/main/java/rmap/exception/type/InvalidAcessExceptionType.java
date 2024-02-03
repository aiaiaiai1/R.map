package rmap.exception.type;

import lombok.Getter;

@Getter
public enum InvalidAcessExceptionType implements ExceptionType {
    NOT_MATCH_NOTIONFOLDER_AND_NOTION(1000,
            "잘못된 접근 입니다.",
            "해당 노션 폴더에 소스 노션이 존재 하지 않는 경우."
    ),
    DUPLICATE_IDS(
            1001,
            "잘못된 접근 입니다.",
            "중복된 id가 존재하는 경우"
    ),
    ; // 메시지 어떻게 하지...??

    private final int errorCode;
    private final String message;
    private final String description;

    InvalidAcessExceptionType(int errorCode, String message, String description) {
        this.errorCode = errorCode;
        this.message = message;
        this.description = description;
    }
}
