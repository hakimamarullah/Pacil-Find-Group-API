package cs.ui.pacilfindgroup.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

class PacilUserTest {
    @Test
    void testPacilUserShouldHaveNPMAttribute() throws NoSuchFieldException {
        assertDoesNotThrow(() -> PacilUser.class.getDeclaredField("npm"));
        assertEquals(Long.class, PacilUser.class.getDeclaredField("npm").getType());
    }

    @Test
    void testPacilUserShouldHaveUsernameAttribute() throws NoSuchFieldException {
        assertDoesNotThrow(() -> PacilUser.class.getDeclaredField("username"));
        assertEquals(String.class, PacilUser.class.getDeclaredField("username").getType());
    }

    @Test
    void testPacilUserShouldHaveEmailAttribute() throws NoSuchFieldException {
        assertDoesNotThrow(() -> PacilUser.class.getDeclaredField("email"));
        assertEquals(String.class, PacilUser.class.getDeclaredField("email").getType());
    }

    @Test
    void testPacilUserShouldHaveIsSuperuserAttribute() throws NoSuchFieldException {
        assertDoesNotThrow(() -> PacilUser.class.getDeclaredField("isSuperuser"));
        assertEquals(boolean.class, PacilUser.class.getDeclaredField("isSuperuser").getType());
    }

    @Test
    void testPacilUserShouldHaveLineIDAttribute() throws NoSuchFieldException {
        assertDoesNotThrow(() -> PacilUser.class.getDeclaredField("lineID"));
        assertEquals(String.class, PacilUser.class.getDeclaredField("lineID").getType());
    }


}