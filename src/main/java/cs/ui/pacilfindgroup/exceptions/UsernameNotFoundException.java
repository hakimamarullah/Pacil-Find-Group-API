package cs.ui.pacilfindgroup.exceptions;

import org.springframework.http.HttpStatus;

public class UsernameNotFoundException extends ApiRequestException {

    public UsernameNotFoundException(final String message) {
        super(message);
        httpStatus = HttpStatus.NOT_FOUND;
    }
}
