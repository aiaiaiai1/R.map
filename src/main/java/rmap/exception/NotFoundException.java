package rmap.exception;

import org.springframework.http.HttpStatus;
import rmap.exception.type.ExceptionType;

public class NotFoundException extends RmapException {

    public NotFoundException(ExceptionType exceptionType) {
        super(exceptionType, HttpStatus.NOT_FOUND);
    }
}
