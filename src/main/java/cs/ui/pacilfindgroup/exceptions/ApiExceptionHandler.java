package cs.ui.pacilfindgroup.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.sql.Timestamp;

@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(ApiRequestException.class)
    public ResponseEntity<Object> handlerApiRequestException(ApiRequestException e) {
        var ex = new ExceptionPayload();
        ex.setMessage(e.getMessage());
        ex.setError(e.getHttpStatus());
        ex.setStatusCode(e.getHttpStatus().value());
        ex.setDate(new Timestamp(System.currentTimeMillis()));
        return ResponseEntity.status(e.getHttpStatus()).body(ex);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handlerIllegalArgument(IllegalArgumentException e) {
        var ex = new ExceptionPayload();
        ex.setMessage(e.getMessage());
        ex.setError(HttpStatus.BAD_REQUEST);
        ex.setStatusCode(HttpStatus.BAD_REQUEST.value());
        ex.setDate(new Timestamp(System.currentTimeMillis()));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Object> handlerIllegalState(IllegalStateException e) {
        var ex = new ExceptionPayload();
        ex.setMessage(e.getMessage());
        ex.setError(HttpStatus.BAD_REQUEST);
        ex.setStatusCode(HttpStatus.BAD_REQUEST.value());
        ex.setDate(new Timestamp(System.currentTimeMillis()));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex);
    }


    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<Object> handlerNullPointer(NullPointerException e) {
        var ex = new ExceptionPayload();
        ex.setMessage("Null Pointer has been issued, Please make sure your input is valid");
        ex.setError(HttpStatus.NOT_FOUND);
        ex.setStatusCode(HttpStatus.NOT_FOUND.value());
        ex.setDate(new Timestamp(System.currentTimeMillis()));
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex);
    }


}
