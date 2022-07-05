package cs.ui.pacilfindgroup.repository;

import cs.ui.pacilfindgroup.model.Course;
import cs.ui.pacilfindgroup.model.Group;
import cs.ui.pacilfindgroup.model.PacilUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.core.parameters.P;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@RunWith(SpringRunner.class)
@DataJpaTest
class GroupRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private GroupRepository groupRepository;


    private PacilUser user;


    private Course course;
    private Course course2;

    private Group group;

    @BeforeEach
    void setUp(){

        group = new Group();
        user = new PacilUser();
        course = new Course();
        course2 = new Course();

        user.setNpm(1906293051L);
        user.setSuperuser(true);
        user.setPassword("halo");
        user.setUsername("hai");
        user.setEmail("hakim@ui.ac.id");
        user.setLineID("line");
        user.setGroups(new HashSet<>());

        course.setCourseId("CS");
        course.setCourseName("DAA");
        course.setGroups(new HashSet<>());

        course2.setCourseId("CSUI");
        course2.setCourseName("DAAB");
        course2.setGroups(new HashSet<>());

        group.setCompleted(false);
        group.setAuthor(user);
        group.setCourse(course);

        entityManager.persistAndFlush(course2);
        entityManager.persistAndFlush(user);
        entityManager.persistAndFlush(course);
        entityManager.persistAndFlush(group);
    }

    @Test
    void save() {
        Group saved = groupRepository.save(group);

        assertEquals(user, saved.getAuthor());
        assertEquals(course, saved.getCourse());
    }

    @Test
    void findAllByCompletedIsFalse() {
        var group = groupRepository.findAllByCompletedIsFalse();

        assertEquals(1, group.size());

    }

    @Test
    void findById() {
        int id = (int) entityManager.getId(group);
        var group = groupRepository.findById(id);

        assertNotNull(group);
    }

    @Test
    void existsByAuthorAndCourseAndCompletedFalse() {
        var found = groupRepository.existsByAuthorAndCourseAndCompletedFalse(user,course);

        assertTrue(found);
    }

    @Test
    void existsByAuthorAndCourseAndCompletedFalseShouldReturnFalse() {
        var found = groupRepository.existsByAuthorAndCourseAndCompletedFalse(user,course2);

        assertFalse(found);
    }

    @Test
    void findAllByCourse_CourseNameContainsIgnoreCaseAndCompletedFalse() {
        var group = groupRepository
                .findAllByCourse_CourseNameContainsIgnoreCaseAndCompletedFalse("da");

        assertEquals(1, group.size());
    }
}