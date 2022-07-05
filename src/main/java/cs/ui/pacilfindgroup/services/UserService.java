package cs.ui.pacilfindgroup.services;

import cs.ui.pacilfindgroup.dto.UserDTO;
import cs.ui.pacilfindgroup.exceptions.ApiRequestException;
import cs.ui.pacilfindgroup.model.Group;
import cs.ui.pacilfindgroup.model.PacilUser;

import java.util.Set;

public interface UserService {
    PacilUser save(UserDTO user) throws ApiRequestException;

    PacilUser getUserByUsername(String username);

    Set<Group> getAllGroup(String username);
}
