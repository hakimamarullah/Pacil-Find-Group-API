package cs.ui.pacilfindgroup.exceptions;

import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends ApiRequestException {
    public ResourceNotFoundException(final String message) {
        super(message);
        httpStatus = HttpStatus.NOT_FOUND;
    }
}
