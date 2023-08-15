package ir.mohaymen.iris.handler;

import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;

@RestControllerAdvice
public class GlobalExceptionHandler {
    Logger logger= LoggerFactory.getLogger(GlobalExceptionHandler.class);
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<?> entityNotFound (EntityNotFoundException e) {
        log(e);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<?> nullPointer(NullPointerException e) {
        log(e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }
    @ExceptionHandler({
            ResponseStatusException.class ,
            HttpClientErrorException.class
    })
    public ResponseEntity<?> badData (Exception e) {
        log(e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> hsbd(Exception e)
    {
        log(e);
        return ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).body(e.getMessage());
    }
    private void log(Exception e){
        logger.error("Not found exception message:"+e.getMessage());
        var stackTrace =Arrays.stream(e.getStackTrace()).limit(5).map(StackTraceElement::toString).toArray(String[]::new);
        logger.error(String.join("\n", stackTrace));
    }
}
