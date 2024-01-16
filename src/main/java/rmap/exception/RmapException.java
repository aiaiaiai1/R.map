package rmap.exception;

import rmap.exception.type.ExceptionType;

public class RmapException extends RuntimeException {
    private final ExceptionType exceptionType;

    public RmapException(ExceptionType exceptionType) {
        super(exceptionType.getMessage());
        this.exceptionType = exceptionType;
    }

    public int getErrorCode() {
        return exceptionType.getErrorCode();
    }
}
