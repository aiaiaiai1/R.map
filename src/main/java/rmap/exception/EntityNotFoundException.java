package rmap.exception;

import rmap.exception.type.ExceptionType;

public class EntityNotFoundException extends RmapException {

    public EntityNotFoundException(ExceptionType exceptionType) {
        super(exceptionType);
    }
}
