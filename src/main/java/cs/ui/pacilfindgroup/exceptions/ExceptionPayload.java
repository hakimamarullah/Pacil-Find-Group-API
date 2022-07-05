package cs.ui.pacilfindgroup.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExceptionPayload {
    private String message;
    private HttpStatus error;
    private Timestamp date;
    private int statusCode;
}
