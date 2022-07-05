package cs.ui.pacilfindgroup.exceptions;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;

class JWTExceptionTest {
    private Class<?> jwtExceptionClass;

    private JWTException jwtException;
    @BeforeEach
    void setUp() throws ClassNotFoundException {
        jwtExceptionClass = Class.
                forName("cs.ui.pacilfindgroup.exceptions.JWTException");
        jwtException = new JWTException("Jwt Exception");

    }

    @Test
    void testJwtExceptionShouldExtendApiRequestException(){
        assertEquals(ApiRequestException.class,jwtExceptionClass.getSuperclass());
    }

    @Test
    void testJwtExceptionShouldShouldReturnValidMessage(){
        assertEquals("Jwt Exception", jwtException.getMessage());
    }

    @Test
    void testJwtExceptionShouldShouldHttpBadRequest(){
        assertEquals(HttpStatus.BAD_REQUEST, jwtException.getHttpStatus());
    }
}