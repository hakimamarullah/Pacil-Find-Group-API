package cs.ui.pacilfindgroup.repository;

import cs.ui.pacilfindgroup.model.Application;
import cs.ui.pacilfindgroup.model.ApplicationType;
import cs.ui.pacilfindgroup.model.Group;
import cs.ui.pacilfindgroup.model.PacilUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplicantRepository extends JpaRepository<Application, Long> {
    Application findById(long id);

    List<Application> findApplicantByPacilGroup(Group pacilGroup);

    List<Application> findApplicantByPacilUser(PacilUser pacilUser);

    List<Application> findApplicantByPacilGroupAndStatus(Group pacilGroup, ApplicationType status);

    List<Application> findApplicantByPacilUserAndStatus(PacilUser pacilUser, ApplicationType status);

    boolean existsApplicantByPacilUserAndPacilGroupAndStatusIs(PacilUser pacilUser, Group group, ApplicationType type);
}
