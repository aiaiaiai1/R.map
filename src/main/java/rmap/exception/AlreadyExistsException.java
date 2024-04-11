package rmap.exception;

import rmap.exception.type.ExceptionType;

public class AlreadyExistsException extends RmapException {
    public AlreadyExistsException(ExceptionType exceptionType) {
        super(exceptionType);
    }
}
