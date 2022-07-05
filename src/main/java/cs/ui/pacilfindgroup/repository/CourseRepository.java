package cs.ui.pacilfindgroup.repository;

import cs.ui.pacilfindgroup.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, String> {

    Course findCourseByCourseId(String id);

    Course save(Course course);


    List<Course> findCourseByCourseNameContainsIgnoreCase(String name);

    List<Course> findAll();

    boolean existsCourseByCourseId(String id);
}
