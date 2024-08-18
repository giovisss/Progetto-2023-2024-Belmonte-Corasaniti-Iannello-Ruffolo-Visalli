package unical.enterprise.jokibackend.Component;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.ws.rs.NotFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value={NotFoundException.class})
    public ResponseEntity<String> handleNotFoundException(NotFoundException e) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(value={IllegalArgumentException.class})
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(value={RuntimeException.class})
    public ResponseEntity<String> handleRuntimeException(RuntimeException e) {
        return ResponseEntity.internalServerError().body(e.getMessage());
    }

    @ExceptionHandler(value={Exception.class})
    public ResponseEntity<String> handleException(Exception e) {
        return ResponseEntity.badRequest().body("An error occurred");
    }

    @ExceptionHandler(value={Throwable.class})
    public ResponseEntity<String> handleThrowable(Throwable e) {
        return ResponseEntity.badRequest().body("An error occurred");
    }

    @ExceptionHandler(value={Error.class})
    public ResponseEntity<String> handleError(Error e) {
        return ResponseEntity.badRequest().body("An error occurred");
    }
}
