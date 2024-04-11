package rmap.exception;

import rmap.exception.type.ExceptionType;

public class EtcException extends RmapException {
    public EtcException(ExceptionType exceptionType) {
        super(exceptionType);
    }
}
