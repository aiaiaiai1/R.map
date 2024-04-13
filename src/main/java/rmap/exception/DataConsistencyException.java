package rmap.exception;

import rmap.exception.type.ExceptionType;

public class DataConsistencyException extends RmapException {

    public DataConsistencyException(ExceptionType exceptionType) {
        super(exceptionType);
    }
}
