package cs.ui.pacilfindgroup.services;

import cs.ui.pacilfindgroup.dto.GroupRequest;
import cs.ui.pacilfindgroup.exceptions.DuplicateRecordException;
import cs.ui.pacilfindgroup.exceptions.ResourceNotFoundException;
import cs.ui.pacilfindgroup.model.Course;
import cs.ui.pacilfindgroup.model.Group;
import cs.ui.pacilfindgroup.model.PacilUser;
import cs.ui.pacilfindgroup.repository.CourseRepository;
import cs.ui.pacilfindgroup.repository.GroupRepository;
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
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class GroupServiceImplTest {

    @Autowired
    private GroupServiceImpl groupService;

    @MockBean
    private PacilUserRepository pacilUserRepository;

    @MockBean
    private GroupRepository groupRepository;

    @MockBean
    private CourseRepository courseRepository;

    private Group group;
    private PacilUser pacilUser;
    private Course course;
    private GroupRequest groupRequest;

    @BeforeEach
    void setUp(){
        group = new Group();
        pacilUser = new PacilUser();
        groupRequest = new GroupRequest("hakim","CSUI","A",10);
        course = new Course("CSUI","DESAIN", new HashSet<>());

        pacilUser.setNpm(1906293051L);
        group.setCompleted(false);
        group.setGroupId(1);
        group.setAuthor(pacilUser);
        group.setCourse(course);
    }
    @Test
    void testCreateGroupSuccess() {
        when(pacilUserRepository.findByUsername(any(String.class))).thenReturn(pacilUser);
        when(courseRepository.findCourseByCourseId(any(String.class))).thenReturn(course);
        when(groupRepository.save(any(Group.class))).thenReturn(group);

        var savedGroup = groupService.createGroup(groupRequest);
        assertNotNull(savedGroup);
        assertEquals(course,savedGroup.getCourse());
        assertEquals(pacilUser, savedGroup.getAuthor());
        assertFalse(savedGroup.isCompleted());
    }

    @Test
    void testCreateGroupCourseIdNotFoundShouldThrowException() {
        when(pacilUserRepository.findByUsername(any(String.class))).thenReturn(pacilUser);
        when(courseRepository.findCourseByCourseId(any(String.class))).thenReturn(null);
        when(groupRepository.save(any(Group.class))).thenReturn(group);

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                ()-> groupService.createGroup(groupRequest));
        assertEquals("Course with ID: CSUI Not Found",ex.getMessage());
        verify(groupRepository, times(0)).save(any(Group.class));
    }

    @Test
    void testCreateGroupForTheSameCourseOnGoingShouldThrowException() {
        when(pacilUserRepository.findByUsername(any(String.class))).thenReturn(pacilUser);
        when(courseRepository.findCourseByCourseId(any(String.class))).thenReturn(course);
        when(groupRepository.existsByAuthorAndCourseAndCompletedFalse(any(PacilUser.class), any(Course.class)))
                .thenReturn(true);

        DuplicateRecordException ex = assertThrows(DuplicateRecordException.class,
                ()-> groupService.createGroup(groupRequest));
        assertEquals("You have created a group for this course", ex.getMessage());
        verify(groupRepository, times(1))
                .existsByAuthorAndCourseAndCompletedFalse(any(PacilUser.class), any(Course.class));
    }

    @Test
    void testFindByIdShouldReturnNonNull() {
        when(groupRepository.findById(1)).thenReturn(group);

        assertNotNull(groupService.findById(1));
    }

    @Test
    void testcloseRecruitmentGroupByIdSuccess() {
        when(groupRepository.findById(1)).thenReturn(group);

        groupService.closeRecruitmentGroupById(1);
        assertTrue(group.isCompleted());
    }

    @Test
    void testcloseRecruitmentGroupByIdThrowResourceNotFoundException() {
        when(groupRepository.findById(1)).thenReturn(null);

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                ()-> groupService.closeRecruitmentGroupById(1));
        assertEquals("Group not found with ID: 1", ex.getMessage());
        verify(groupRepository, times(1)).findById(1);
    }

    @Test
    void testDeleteByIdSuccess() {
        doNothing().when(groupRepository).deleteById(1);
        when(groupRepository.findById(anyInt())).thenReturn(group);

        groupService.deleteById(1);
        verify(groupRepository, times(1)).deleteById(1);
    }

    @Test
    void testDeleteByIdFailed() {
        doThrow(EmptyResultDataAccessException.class).when(groupRepository).deleteById(1);

        assertThrows(ResourceNotFoundException.class, () -> groupService.deleteById(1));
    }

    @Test
    void testFindByNameContainsShouldReturnSomeCourse() {
        when(groupRepository.findAllByCourse_CourseNameContainsIgnoreCaseAndCompletedFalse(any(String.class)))
                .thenReturn(List.of(group));
        assertNotNull(groupService.findAllByCourseName("Desain"));
        assertEquals(1, groupService.findAllByCourseName("Desain").size());
    }

    @Test
    void testFindByNameContainsShouldReturnListWithSizeZero() {
        when(groupRepository.findAllByCourse_CourseNameContainsIgnoreCaseAndCompletedFalse(any(String.class)))
                .thenReturn(new ArrayList<>());
        assertNotNull(groupService.findAllByCourseName("Desain"));
        assertEquals(0, groupService.findAllByCourseName("Desain").size());
    }

    @Test
    void testFindAllShouldReturnNonNull() {
        when(groupRepository.findAllByCompletedIsFalse())
                .thenReturn(List.of(group));
        assertNotNull(groupService.findAll());
    }

    @Test
    void testFindAllShouldReturnEmptyList() {
        when(groupRepository.findAllByCompletedIsFalse())
                .thenReturn(new ArrayList<>());
        assertNotNull(groupService.findAll());
        assertEquals(0, groupService.findAll().size());
    }
}