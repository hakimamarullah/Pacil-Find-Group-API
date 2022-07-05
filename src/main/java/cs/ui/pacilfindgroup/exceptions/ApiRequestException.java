package cs.ui.pacilfindgroup.exceptions;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Setter
@Getter
public abstract class ApiRequestException extends RuntimeException {
    protected HttpStatus httpStatus;

    protected ApiRequestException(String message) {
        super(message);
        httpStatus = HttpStatus.BAD_REQUEST;
    }

}
