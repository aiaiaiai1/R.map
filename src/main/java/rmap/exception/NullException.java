package rmap.exception;

import rmap.exception.type.ExceptionType;

public class NullException extends BadRequestException {
    public NullException(ExceptionType exceptionType) {
        super(exceptionType);
    }
}
