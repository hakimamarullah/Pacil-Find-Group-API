package cs.ui.pacilfindgroup.repository;

import cs.ui.pacilfindgroup.model.Course;
import cs.ui.pacilfindgroup.model.Group;
import cs.ui.pacilfindgroup.model.PacilUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupRepository extends JpaRepository<Group, Integer> {

    @Override
    Group save(Group group);

    List<Group> findAllByCompletedIsFalse();

    List<Group> findAllByCompletedFalse();

    Group findById(int id);

    boolean existsByAuthorAndCourseAndCompletedFalse(PacilUser author, Course course);

    List<Group> findAllByCourse_CourseNameContainsIgnoreCaseAndCompletedFalse(String courseName);

}
