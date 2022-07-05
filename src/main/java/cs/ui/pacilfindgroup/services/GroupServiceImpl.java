package cs.ui.pacilfindgroup.services;

import cs.ui.pacilfindgroup.dto.GroupRequest;
import cs.ui.pacilfindgroup.exceptions.DuplicateRecordException;
import cs.ui.pacilfindgroup.exceptions.ResourceNotFoundException;
import cs.ui.pacilfindgroup.model.Group;
import cs.ui.pacilfindgroup.repository.CourseRepository;
import cs.ui.pacilfindgroup.repository.GroupRepository;
import cs.ui.pacilfindgroup.repository.PacilUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupServiceImpl implements GroupService {

    @Autowired
    private PacilUserRepository pacilUserRepository;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Override
    public Group createGroup(GroupRequest request) {
        var user = pacilUserRepository.findByUsername(request.getUsername());
        var course = courseRepository.findCourseByCourseId(request.getCourseId());

        if (course == null)
            throw new ResourceNotFoundException("Course with ID: " + request.getCourseId() + " Not Found");

        if (groupRepository.existsByAuthorAndCourseAndCompletedFalse(user, course))
            throw new DuplicateRecordException("You have created a group for this course");

        var group = new Group();
        group.setAuthor(user);
        group.setKelas(request.getKelas());
        group.setMaxMember(request.getMaxMember());
        group.setCourse(course);
        course.addGroup(group);
        user.addGroup(group);
        groupRepository.save(group);
        courseRepository.save(course);
        pacilUserRepository.save(user);
        return group;
    }

    @Override
    public Group findById(final int id) {
        return groupRepository.findById(id);
    }

    @Override
    public void closeRecruitmentGroupById(int id) {
        var group = groupRepository.findById(id);

        if (group == null)
            throw new ResourceNotFoundException("Group not found with ID: " + id);
        group.setCompleted(true);
        groupRepository.save(group);
    }

    @Override
    public void deleteById(final int id) {
        try {
            groupRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException("Group doesn't exist with the given id");
        }

    }

    @Override
    public List<Group> findAllByCourseName(final String courseName) {
        return groupRepository.findAllByCourse_CourseNameContainsIgnoreCaseAndCompletedFalse(courseName);
    }

    @Override
    public List<Group> findAll() {
        return groupRepository.findAllByCompletedIsFalse();
    }
}
