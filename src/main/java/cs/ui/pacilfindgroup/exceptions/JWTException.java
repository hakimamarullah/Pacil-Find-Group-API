package cs.ui.pacilfindgroup.exceptions;

import org.springframework.http.HttpStatus;

public class JWTException extends ApiRequestException {

    public JWTException(final String message) {
        super(message);
        httpStatus = HttpStatus.BAD_REQUEST;
    }
}
