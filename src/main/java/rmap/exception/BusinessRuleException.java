package rmap.exception;

import rmap.exception.type.ExceptionType;

public class BusinessRuleException extends RmapException {
    public BusinessRuleException(ExceptionType exceptionType) {
        super(exceptionType);
    }
}
