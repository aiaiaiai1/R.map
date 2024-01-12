package rmap;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import rmap.exception.RmapException;
import rmap.response.ErrorResponse;

@RestControllerAdvice
public class ExceptionController {

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleRawException(IllegalArgumentException e) {
        return ResponseEntity.badRequest()
                .body(new ErrorResponse(0, e.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleRmapException(RmapException e) {
        return ResponseEntity.status(e.getHttpStatus())
                .body(new ErrorResponse(e.getErrorCode(), e.getMessage()));
    }
}
