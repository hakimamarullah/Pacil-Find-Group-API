package cs.ui.pacilfindgroup.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CourseDTOTest {

    private CourseDTO courseDTO;

    @BeforeEach
    void setUp() {
        courseDTO = new CourseDTO();
        courseDTO.setUsername("Test");
        courseDTO.setCourseId("CSUIDAA");
        courseDTO.setCourseName("Desain Algoritma");
    }

    @Test
    void testGetMethod() {
        assertEquals("Test", courseDTO.getUsername());
        assertEquals("CSUIDAA", courseDTO.getCourseId());
        assertEquals("Desain Algoritma", courseDTO.getCourseName());
    }

    @Test
    void testCourseDTOShouldHaveAttributeCourseId() {
        assertDoesNotThrow(() -> CourseDTO.class.getDeclaredField("courseId"));
    }

    @Test
    void testCourseDTOShouldHaveAttributeCourseName() {
        assertDoesNotThrow(() -> CourseDTO.class.getDeclaredField("courseName"));
    }

    @Test
    void testCourseDTOShouldHaveAttributeUsername() {
        assertDoesNotThrow(() -> CourseDTO.class.getDeclaredField("username"));
    }


}