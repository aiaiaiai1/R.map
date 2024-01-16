package rmap.exception;

import rmap.exception.type.ExceptionType;

public class InvalidAcessException extends RmapException {

    public InvalidAcessException(ExceptionType exceptionType) {
        super(exceptionType);
    }
}
