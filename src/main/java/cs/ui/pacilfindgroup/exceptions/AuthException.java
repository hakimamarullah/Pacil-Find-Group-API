package cs.ui.pacilfindgroup.exceptions;

import org.springframework.http.HttpStatus;


public class AuthException extends ApiRequestException {
    public AuthException(final String message) {
        super(message);
        httpStatus = HttpStatus.FORBIDDEN;
    }

}
