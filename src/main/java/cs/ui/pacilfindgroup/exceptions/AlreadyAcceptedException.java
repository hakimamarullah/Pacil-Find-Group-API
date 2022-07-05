package cs.ui.pacilfindgroup.exceptions;

import org.springframework.http.HttpStatus;

public class AlreadyAcceptedException extends ApiRequestException {

    public AlreadyAcceptedException(String s) {
        super(s);
        httpStatus = HttpStatus.CONFLICT;
    }
}
