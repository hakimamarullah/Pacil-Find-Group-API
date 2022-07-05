package cs.ui.pacilfindgroup.exceptions;

import org.springframework.http.HttpStatus;

public class EmailInvalidFormatException extends ApiRequestException {

    public EmailInvalidFormatException(final String s) {
        super(s);
        httpStatus = HttpStatus.BAD_REQUEST;
    }

}
