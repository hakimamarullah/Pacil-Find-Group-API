package cs.ui.pacilfindgroup.services;

import cs.ui.pacilfindgroup.dto.CourseDTO;
import cs.ui.pacilfindgroup.exceptions.AuthException;
import cs.ui.pacilfindgroup.exceptions.ResourceNotFoundException;
import cs.ui.pacilfindgroup.model.Course;
import cs.ui.pacilfindgroup.repository.CourseRepository;
import cs.ui.pacilfindgroup.repository.PacilUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class CourseServiceImplTest {

    @Autowired
    private CourseServiceImpl courseService;

    @MockBean
    private CourseRepository courseRepository;

    @MockBean
    private PacilUserRepository pacilUserRepository;

    private Course course;
    private CourseDTO courseDTO;

    @BeforeEach
    void setUp() {
        course = new Course();
        course.setCourseId("CSUI");
        course.setCourseName("Desain Algoritma");

        courseDTO = new CourseDTO();
        courseDTO.setCourseId("CSUI");
        courseDTO.setCourseName("Desain Algoritma");
        courseDTO.setUsername("hakim");
    }

    @Test
    void testSaveCourseWithValidData() {
        when(courseRepository.existsCourseByCourseId(courseDTO.getCourseId())).thenReturn(false);
        when(courseRepository.save(any(Course.class))).thenReturn(course);
        when(pacilUserRepository
                .existsPacilUsersByUsernameAndIsSuperuserTrue(any(String.class))).thenReturn(true);

        var savedCourse = courseService.save(courseDTO);
        assertEquals(course.getCourseId(), savedCourse.getCourseId());
        assertEquals(course.getCourseName(), savedCourse.getCourseName());
    }

    @Test
    void testSaveCourseByNonSuperuserShouldThrowException() {
        when(courseRepository.existsCourseByCourseId(courseDTO.getCourseId())).thenReturn(false);
        when(courseRepository.save(any(Course.class))).thenReturn(course);
        when(pacilUserRepository
                .existsPacilUsersByUsernameAndIsSuperuserTrue(any(String.class))).thenReturn(false);

      AuthException ex = assertThrows(AuthException.class,()->courseService.save(courseDTO));
      assertEquals("Permission is denied, superuser only", ex.getMessage());
    }

    @Test
    void testSaveCourseWithNullIdShouldThrowIllegalArgumentException() {
        courseDTO.setCourseId(null);
        when(courseRepository.existsCourseByCourseId(courseDTO.getCourseId())).thenReturn(false);
        when(courseRepository.save(any(Course.class))).thenReturn(course);
        when(pacilUserRepository
                .existsPacilUsersByUsernameAndIsSuperuserTrue(any(String.class))).thenReturn(true);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> courseService.save(courseDTO));
        assertEquals("courseId can't be blank", ex.getMessage());
    }

    @Test
    void testSaveCourseIdExistsShouldThrowIllegalArgumentException() {
        when(courseRepository.existsCourseByCourseId(courseDTO.getCourseId())).thenReturn(true);
        when(courseRepository.save(any(Course.class))).thenReturn(course);
        when(pacilUserRepository
                .existsPacilUsersByUsernameAndIsSuperuserTrue(any(String.class))).thenReturn(true);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> courseService.save(courseDTO));
        assertEquals("courseId: CSUI already registered", ex.getMessage());
    }

    @Test
    void testfindByNameContainsShouldReturnSomeCourse() {
        when(courseRepository.findCourseByCourseNameContainsIgnoreCase(any(String.class)))
                .thenReturn(List.of(course));
        assertNotNull(courseService.findByNameContains("Desain"));
        assertEquals(1,courseService.findByNameContains("Desain").size());
    }

    @Test
    void testfindByNameContainsShouldReturnListWithSizeZero() {
        when(courseRepository.findCourseByCourseNameContainsIgnoreCase(any(String.class)))
                .thenReturn(new ArrayList<>());
        assertNotNull(courseService.findByNameContains("D"));
        assertEquals(0,courseService.findByNameContains("Desain").size());
    }

    @Test
    void testFindAllShouldReturnNonNull() {
        when(courseRepository.findAll())
                .thenReturn(List.of(course));
        assertNotNull(courseService.findAll());
    }

    @Test
    void testFindAllShouldReturnEmptyList() {
        when(courseRepository.findAll())
                .thenReturn(new ArrayList<>());
        assertNotNull(courseService.findAll());
        assertEquals(0, courseService.findAll().size());
    }


    @Test
    void testFindByIdShouldReturnNonNull() {
        when(courseRepository.findCourseByCourseId(any(String.class))).thenReturn(course);

        assertNotNull(courseService.findById("CSUIDAA"));
    }

    @Test
    void testDeleteByIdSuccess() {
        doNothing().when(courseRepository).deleteById(any(String.class));
        when(pacilUserRepository
                .existsPacilUsersByUsernameAndIsSuperuserTrue(any(String.class))).thenReturn(true);

        courseService.deleteById("CSUI", "Hakim");
        verify(courseRepository, times(1)).deleteById(any(String.class));
    }

    @Test
    void testDeleteByNonExistingIdShouldThrowResourceNotFoundException() {
        doThrow(EmptyResultDataAccessException.class).when(courseRepository)
                .deleteById(any(String.class));
        when(pacilUserRepository
                .existsPacilUsersByUsernameAndIsSuperuserTrue(any(String.class))).thenReturn(true);

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> courseService.deleteById("CSUI", "Hakim"));
        assertEquals("Course with ID: CSUI Not Found", ex.getMessage());
        verify(courseRepository, times(1)).deleteById(any(String.class));
    }

    @Test
    void testDeleteByNonSuperuserShouldThrowException() {
        doThrow(EmptyResultDataAccessException.class).when(courseRepository)
                .deleteById(any(String.class));
        when(pacilUserRepository
                .existsPacilUsersByUsernameAndIsSuperuserTrue(any(String.class))).thenReturn(false);

        AuthException ex = assertThrows(AuthException.class,
                () -> courseService.deleteById("CSUI", "Hakim"));
        assertEquals("Permission is denied, superuser only", ex.getMessage());
        verify(courseRepository, times(0)).deleteById(any(String.class));
    }
}