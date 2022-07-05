package cs.ui.pacilfindgroup.repository;

import cs.ui.pacilfindgroup.model.Course;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@RunWith(SpringRunner.class)
@DataJpaTest
class CourseRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CourseRepository courseRepository;

    private Course course;
    @BeforeEach
    void setUp(){
        course  = new Course();
        course.setCourseId("CS");
        course.setCourseName("DAA");
    }
    @Test
    void testFindCourseByCourseIdShouldReturnCourse() {
        entityManager.persist(course);
        entityManager.flush();

        var savedCourse = courseRepository.findCourseByCourseId(course.getCourseId());

        assertNotNull(savedCourse);
        assertEquals("CS", savedCourse.getCourseId());
    }

    @Test
    void testFindCourseByCourseIdShouldReturnNull() {
        entityManager.persist(course);
        entityManager.flush();

        var savedCourse = courseRepository.findCourseByCourseId("HA");

        assertNull(savedCourse);

    }

    @Test
    void save() {
        Course savedCourse = courseRepository.save(course);

        assertEquals("CS", savedCourse.getCourseId());
        assertEquals("DAA", savedCourse.getCourseName());
    }

    @Test
    void findCourseByCourseNameContainsIgnoreCase() {
        entityManager.persist(course);
        entityManager.flush();

        List<Course> found = courseRepository.findCourseByCourseNameContainsIgnoreCase("da");
        List<Course> found2 = courseRepository.findCourseByCourseNameContainsIgnoreCase("abc");

        assertEquals(1,found.size());
        assertEquals(0,found2.size());

    }

    @Test
    void findAll() {
        entityManager.persist(course);
        entityManager.flush();

        List<Course> courses = courseRepository.findAll();

        assertEquals(1, courses.size());
    }

    @Test
    void existsCourseByCourseId() {
        entityManager.persist(course);
        entityManager.flush();

        var savedCourse = courseRepository.existsCourseByCourseId("CS");
        var course2 = courseRepository.existsCourseByCourseId("SC");

        assertFalse(course2);
        assertTrue(savedCourse);
    }
}