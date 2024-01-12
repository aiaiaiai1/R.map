package rmap.exception;

import org.springframework.http.HttpStatus;
import rmap.exception.type.ExceptionType;

public class BadRequestException extends RmapException{

    public BadRequestException(ExceptionType exceptionType) {
        super(exceptionType, HttpStatus.BAD_REQUEST);
    }
}
