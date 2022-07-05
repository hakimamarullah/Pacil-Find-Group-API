package cs.ui.pacilfindgroup.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JwtResponseTest {

    private JwtResponse jwtResponse;

    @BeforeEach
    void setUp(){
        jwtResponse = new JwtResponse();
        jwtResponse.setJwtToken("abcde");
        jwtResponse.setSuperuser(true);
        jwtResponse.setUsername("halo");
    }

    @Test
    void testGetMethod(){
        assertEquals("abcde",jwtResponse.getJwtToken());
        assertEquals("halo",jwtResponse.getUsername());
        assertTrue(jwtResponse.isSuperuser());
    }

    @Test
    void testJwtResponseShouldHaveIsSuperuserAttribute() throws NoSuchFieldException {
        assertDoesNotThrow(() -> JwtResponse.class.getDeclaredField("isSuperuser"));
        assertEquals(boolean.class, JwtResponse.class.getDeclaredField("isSuperuser").getType());
    }

    @Test
    void testJwtResponseShouldHaveUsernameAttribute() throws NoSuchFieldException {
        assertDoesNotThrow(() -> JwtResponse.class.getDeclaredField("username"));
        assertEquals(String.class, JwtResponse.class.getDeclaredField("username").getType());
    }

    @Test
    void testJwtResponseShouldHaveJwtTokenAttribute() throws NoSuchFieldException {
        assertDoesNotThrow(() -> JwtResponse.class.getDeclaredField("jwtToken"));
        assertEquals(String.class, JwtResponse.class.getDeclaredField("jwtToken").getType());
    }
}