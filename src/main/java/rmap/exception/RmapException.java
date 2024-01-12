package rmap.exception;

import org.springframework.http.HttpStatus;
import rmap.exception.type.ExceptionType;

public class RmapException extends RuntimeException {
    private final ExceptionType exceptionType;
    private final HttpStatus httpStatus;

    public RmapException(ExceptionType exceptionType, HttpStatus httpStatus) {
        super(exceptionType.getMessage());
        this.exceptionType = exceptionType;
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public int getErrorCode() {
        return exceptionType.getErrorCode();
    }
}
