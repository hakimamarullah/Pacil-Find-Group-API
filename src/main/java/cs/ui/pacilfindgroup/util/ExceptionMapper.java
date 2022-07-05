package cs.ui.pacilfindgroup.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import cs.ui.pacilfindgroup.exceptions.ApiRequestException;
import cs.ui.pacilfindgroup.exceptions.ExceptionPayload;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Timestamp;

public class ExceptionMapper {

    private ExceptionMapper() {

    }

    public static synchronized void mapResponse(final HttpServletResponse response, final ApiRequestException e) throws IOException, ApiRequestException {
        var payload = new ExceptionPayload();
        payload.setMessage(e.getMessage());
        payload.setError(HttpStatus.BAD_REQUEST);
        payload.setStatusCode(HttpStatus.BAD_REQUEST.value());
        payload.setDate(new Timestamp(System.currentTimeMillis()));
        OutputStream out = response.getOutputStream();
        var mapper = new ObjectMapper();

        response.setContentType("application/json");
        response.setStatus(e.getHttpStatus().value());

        mapper.registerModule(new JavaTimeModule());
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        mapper.writeValue(out, payload);
        out.flush();
    }
}
