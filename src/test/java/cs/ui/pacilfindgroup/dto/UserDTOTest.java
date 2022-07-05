package cs.ui.pacilfindgroup.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

class UserDTOTest {
    @Test
    void testUerDTOShouldHaveNPMAttribute() throws NoSuchFieldException {
        assertDoesNotThrow(() -> UserDTO.class.getDeclaredField("npm"));
        assertEquals(Long.class, UserDTO.class.getDeclaredField("npm").getType());
    }


    @Test
    void testUerDTOShouldHaveEmailAttribute() throws NoSuchFieldException {
        assertDoesNotThrow(() -> UserDTO.class.getDeclaredField("email"));
        assertEquals(String.class, UserDTO.class.getDeclaredField("email").getType());
    }

    @Test
    void testUerDTOShouldHaveIsSuperuserAttribute() throws NoSuchFieldException {
        assertDoesNotThrow(() -> UserDTO.class.getDeclaredField("isSuperuser"));
        assertEquals(boolean.class, UserDTO.class.getDeclaredField("isSuperuser").getType());
    }

    @Test
    void testUerDTOShouldHaveLineIDAttribute() throws NoSuchFieldException {
        assertDoesNotThrow(() -> UserDTO.class.getDeclaredField("lineID"));
        assertEquals(String.class, UserDTO.class.getDeclaredField("lineID").getType());
    }


}