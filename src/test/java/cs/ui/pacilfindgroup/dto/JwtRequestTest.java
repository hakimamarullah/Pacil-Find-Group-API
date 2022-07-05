package cs.ui.pacilfindgroup.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

class JwtRequestTest {

    private JwtRequest jwtRequest;

    @BeforeEach
    void setUp(){
        jwtRequest = new JwtRequest();
        jwtRequest.setPassword("halo");
        jwtRequest.setUsername("halo");
    }

    @Test
    void testGetMethod(){
        assertEquals("halo",jwtRequest.getPassword());
        assertEquals("halo",jwtRequest.getUsername());
    }

    @Test
    void testJwtResponseShouldHaveUsernameAttribute() throws NoSuchFieldException {
        assertDoesNotThrow(() -> JwtRequest.class.getDeclaredField("username"));
        assertEquals(String.class, JwtRequest.class.getDeclaredField("username").getType());
    }

    @Test
    void testJwtResponseShouldHavePasswordAttribute() throws NoSuchFieldException {
        assertDoesNotThrow(() -> JwtRequest.class.getDeclaredField("password"));
        assertEquals(String.class, JwtRequest.class.getDeclaredField("password").getType());
    }

}