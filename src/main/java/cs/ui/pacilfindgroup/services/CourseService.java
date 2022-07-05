package cs.ui.pacilfindgroup.services;

import cs.ui.pacilfindgroup.dto.CourseDTO;
import cs.ui.pacilfindgroup.model.Course;

import java.util.List;

public interface CourseService {
    Course save(CourseDTO courseDTO);

    List<Course> findByNameContains(String name);

    List<Course> findAll();

    Course findById(String id);

    void deleteById(String id, String username);
}
