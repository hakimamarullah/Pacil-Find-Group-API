package cs.ui.pacilfindgroup.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ApplicationDTOTest {

    private ApplicationDTO applicationDTO;

    @BeforeEach
    void setUp() {
        applicationDTO = new ApplicationDTO();
        applicationDTO.setGroupId(1);
        applicationDTO.setNpm(1906293051);
    }

    @Test
    void testGetMethod() {
        assertEquals(1, applicationDTO.getGroupId());
        assertEquals(1906293051, applicationDTO.getNpm());
    }

    @Test
    void testCourseDTOShouldHaveAttributeGroupId() {
        assertDoesNotThrow(() -> ApplicationDTO.class.getDeclaredField("groupId"));
    }

    @Test
    void testCourseDTOShouldHaveAttributeNpm() throws NoSuchFieldException {
        assertDoesNotThrow(() -> ApplicationDTO.class.getDeclaredField("npm"));
        assertEquals(long.class, ApplicationDTO.class.getDeclaredField("npm").getType());
    }


}