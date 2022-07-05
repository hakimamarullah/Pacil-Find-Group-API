package cs.ui.pacilfindgroup.exceptions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import static org.junit.jupiter.api.Assertions.*;

class ApiRequestExceptionTest {
    private Class<?> apiRequestException;
    private ApiRequestException exception;
    @BeforeEach
    void setUp() throws ClassNotFoundException {
        apiRequestException = Class
                .forName("cs.ui.pacilfindgroup.exceptions.ApiRequestException");
        exception = new ApiRequestException("Message") {
            @Override
            public HttpStatus getHttpStatus() {
                return super.getHttpStatus();
            }

            @Override
            public void setHttpStatus(HttpStatus httpStatus) {
                super.setHttpStatus(httpStatus);
            }
        };

    }

    @Test
    void testApiRequestExceptionShouldBePublicAbstract() {
        int modifier = apiRequestException.getModifiers();

        assertTrue(Modifier.isPublic(modifier));
        assertTrue(Modifier.isAbstract(modifier));
    }

    @Test
    void testApiRequestExceptioConstructorShoudlBeProtected() {
    Constructor<ApiRequestException>[] constructor = (Constructor<ApiRequestException>[]) apiRequestException.getDeclaredConstructors();
    assertEquals(1, constructor[0].getParameterCount());
    assertEquals(String.class, constructor[0].getParameterTypes()[0]);
    assertTrue(Modifier.isProtected(constructor[0].getModifiers()));
    assertEquals(1, constructor.length);
    }

    @Test
    void testApiRequestExceptionShouldHasHttpStatusField() throws NoSuchFieldException {
        Field field = apiRequestException.getDeclaredField("httpStatus");
        assertTrue(Modifier.isProtected(field.getModifiers()));

    }

    @Test
    void testSetHttpMethod(){
        assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());
        exception.setHttpStatus(HttpStatus.NOT_FOUND);
        assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
    }
}