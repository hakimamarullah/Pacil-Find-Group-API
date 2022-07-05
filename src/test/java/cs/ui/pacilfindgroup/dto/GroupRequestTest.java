package cs.ui.pacilfindgroup.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

class GroupRequestTest {

    private GroupRequest groupRequest;

    @BeforeEach
    void setUp() {
        groupRequest = new GroupRequest();
        groupRequest.setUsername("Test");
        groupRequest.setCourseId("CSUIDAA");
        groupRequest.setKelas("A");
        groupRequest.setMaxMember(10);
    }

    @Test
    void testGetMethod() {
        assertEquals("Test", groupRequest.getUsername());
        assertEquals("CSUIDAA", groupRequest.getCourseId());
        assertEquals("A", groupRequest.getKelas());
        assertEquals(10, groupRequest.getMaxMember());
    }

    @Test
    void testGroupRequestShouldHaveAttributeCourseId() {
        assertDoesNotThrow(() -> GroupRequest.class.getDeclaredField("courseId"));
    }

    @Test
    void testGroupRequestShouldHaveAttributeKelas() {
        assertDoesNotThrow(() -> GroupRequest.class.getDeclaredField("kelas"));
    }

    @Test
    void testGroupRequestShouldHaveAttributeUsername() {
        assertDoesNotThrow(() -> GroupRequest.class.getDeclaredField("username"));
    }

    @Test
    void testGroupRequestShouldHaveAttributeMaxMember() {
        assertDoesNotThrow(() -> GroupRequest.class.getDeclaredField("maxMember"));
    }


}