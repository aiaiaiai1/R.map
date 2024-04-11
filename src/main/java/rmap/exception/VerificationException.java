package rmap.exception;

import rmap.exception.type.ExceptionType;

public class VerificationException extends RmapException {
    public VerificationException(ExceptionType exceptionType) {
        super(exceptionType);
    }
}
