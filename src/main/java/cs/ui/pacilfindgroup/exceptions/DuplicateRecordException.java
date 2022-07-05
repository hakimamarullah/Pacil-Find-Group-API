package cs.ui.pacilfindgroup.exceptions;

import org.springframework.http.HttpStatus;

public class DuplicateRecordException extends ApiRequestException {

    public DuplicateRecordException(String s) {
        super(s);
        httpStatus = HttpStatus.CONFLICT;
    }
}
