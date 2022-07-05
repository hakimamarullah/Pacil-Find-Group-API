package cs.ui.pacilfindgroup.services;

import cs.ui.pacilfindgroup.dto.GroupRequest;
import cs.ui.pacilfindgroup.model.Group;

import java.util.List;

public interface GroupService {
    Group createGroup(GroupRequest request);

    Group findById(int id);

    void closeRecruitmentGroupById(int id);

    void deleteById(int id);

    List<Group> findAllByCourseName(String courseName);

    List<Group> findAll();
}
