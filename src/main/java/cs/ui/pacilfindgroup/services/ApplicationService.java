package cs.ui.pacilfindgroup.services;

import cs.ui.pacilfindgroup.dto.AcceptorDeclineApplicantDTO;
import cs.ui.pacilfindgroup.dto.ApplicationDTO;
import cs.ui.pacilfindgroup.model.Application;
import cs.ui.pacilfindgroup.model.EventMonitor;

import java.util.List;

public interface ApplicationService {
    EventMonitor getApplicationMonitor();

    Application createApplication(ApplicationDTO applicant);

    Application deleteApplication(long id);

    Application acceptOrDeclineApplicant(long id, AcceptorDeclineApplicantDTO typeId);

    List<Application> getAllApplicationByUser(long npm);

    List<Application> getAllApplicationByGroup(int id);

    List<Application> getAllActiveApplicationByUser(long npm);

    List<Application> getAllActiveApplicationByGroup(int id);

}
