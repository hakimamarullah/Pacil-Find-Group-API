package cs.ui.pacilfindgroup.services;

import cs.ui.pacilfindgroup.dto.CourseDTO;
import cs.ui.pacilfindgroup.exceptions.AuthException;
import cs.ui.pacilfindgroup.exceptions.ResourceNotFoundException;
import cs.ui.pacilfindgroup.model.Course;
import cs.ui.pacilfindgroup.repository.CourseRepository;
import cs.ui.pacilfindgroup.repository.PacilUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseServiceImpl implements CourseService {
    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private PacilUserRepository pacilUserRepository;

    @Override
    public Course save(CourseDTO courseDTO) {
        var course = new Course();
        if (!pacilUserRepository.existsPacilUsersByUsernameAndIsSuperuserTrue(courseDTO.getUsername()))
            throw new AuthException("Permission is denied, superuser only");

        if (courseDTO.getCourseId() == null)
            throw new IllegalArgumentException("courseId can't be blank");

        if (courseRepository.existsCourseByCourseId(courseDTO.getCourseId()))
            throw new IllegalArgumentException("courseId: " + courseDTO.getCourseId() + " already registered");


        course.setCourseId(courseDTO.getCourseId());
        course.setCourseName(courseDTO.getCourseName());
        return courseRepository.save(course);
    }

    @Override
    public List<Course> findByNameContains(String name) {

        return courseRepository.findCourseByCourseNameContainsIgnoreCase(name);
    }

    @Override
    public List<Course> findAll() {
        return courseRepository.findAll();
    }

    @Override
    public Course findById(String id) {
        return courseRepository.findCourseByCourseId(id);
    }

    @Override
    public void deleteById(String id, String username) {
        try {
            if (!pacilUserRepository.existsPacilUsersByUsernameAndIsSuperuserTrue(username))
                throw new AuthException("Permission is denied, superuser only");
            courseRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException("Course with ID: " + id + " Not Found");
        }

    }
}
