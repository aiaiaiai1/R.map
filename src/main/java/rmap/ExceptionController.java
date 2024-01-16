package rmap;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import rmap.exception.EntityNotFoundException;
import rmap.exception.RmapException;
import rmap.response.ErrorResponse;

@RestControllerAdvice
@Slf4j
public class ExceptionController {

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleRawException(IllegalArgumentException e) {
        log.info("예외발생", e);
        return ResponseEntity.badRequest()
                .body(new ErrorResponse(0, e.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handlerRaw(MethodArgumentNotValidException e) {
        log.info("예외발생", e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(0, e.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> return404(EntityNotFoundException e) {
        log.info("예외발생", e);
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(e.getErrorCode(), e.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> return500(RmapException e) {
        log.info("예외발생", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(e.getErrorCode(), e.getMessage()));
    }
}
