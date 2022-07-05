package cs.ui.pacilfindgroup.services;

import cs.ui.pacilfindgroup.dto.AcceptorDeclineApplicantDTO;
import cs.ui.pacilfindgroup.dto.ApplicationDTO;
import cs.ui.pacilfindgroup.exceptions.AlreadyAcceptedException;
import cs.ui.pacilfindgroup.exceptions.DuplicateRecordException;
import cs.ui.pacilfindgroup.exceptions.RequestException;
import cs.ui.pacilfindgroup.model.Application;
import cs.ui.pacilfindgroup.model.ApplicationMonitor;
import cs.ui.pacilfindgroup.model.ApplicationType;
import cs.ui.pacilfindgroup.model.EventMonitor;
import cs.ui.pacilfindgroup.repository.ApplicantRepository;
import cs.ui.pacilfindgroup.repository.GroupRepository;
import cs.ui.pacilfindgroup.repository.PacilUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ApplicationServiceImpl implements ApplicationService {

    @Autowired
    private ApplicantRepository applicantRepository;


    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private PacilUserRepository pacilUserRepository;

    @Override
    public EventMonitor getApplicationMonitor() {
        return ApplicationMonitor.getApplicationMonitor();
    }

    EventMonitor applicationMonitor = getApplicationMonitor();

    @Override
    public Application createApplication(ApplicationDTO application) {
        try {
            var group = groupRepository.findById(application.getGroupId());
            var pacilUser = pacilUserRepository.findByNpm(application.getNpm());
            if (applicantRepository.existsApplicantByPacilUserAndPacilGroupAndStatusIs(pacilUser, group, ApplicationType.NEW_APPLICANT)) {
                throw new DuplicateRecordException("You have created an application for this group");
            } else if (group.getMembers().contains(pacilUser)) {
                throw new AlreadyAcceptedException("You are already accepted into group");
            } else if (group.getAuthor().equals(pacilUser)) {
                throw new AlreadyAcceptedException("You are the author of this group");
            }
            var currentApplicant = new Application();
            currentApplicant.setStatus(ApplicationType.NEW_APPLICANT);
            currentApplicant.setPacilUser(pacilUser);
            currentApplicant.setPacilGroup(group);
            applicantRepository.save(currentApplicant);
            applicationMonitor.informAuthor(group, pacilUser, ApplicationType.NEW_APPLICANT);
            group.addApplication(currentApplicant);
            groupRepository.save(group);
            return currentApplicant;
        }catch (Exception e){
            throw new RequestException(e.getMessage());
        }
    }

    @Override
    public Application deleteApplication(long id) {
        var applicant = applicantRepository.findById(id);
        if (applicant.getStatus().equals(ApplicationType.NEW_APPLICANT)) {
            applicationMonitor.informApplicant(applicant.getPacilGroup(), applicant.getPacilUser(), ApplicationType.CANCEL);
            applicantRepository.delete(applicant);
        }
        return applicant;
    }

    @Override
    public Application acceptOrDeclineApplicant(long id, AcceptorDeclineApplicantDTO typeId) {
        var applicant = applicantRepository.findById(id);
        ApplicationType type;
        switch (typeId.getTypeId()) {
            case 1:
                type = ApplicationType.ACCEPTANCE;
                break;
            case 2:
                type = ApplicationType.REJECTION;
                break;
            default:
                type = null;
        }
        var group = groupRepository.findById(applicant.getPacilGroup().getGroupId());
        var pacilUser = pacilUserRepository.findByNpm(applicant.getPacilUser().getNpm());
        if (applicant.getStatus() == ApplicationType.NEW_APPLICANT && type == ApplicationType.ACCEPTANCE) {
            applicant.setStatus(ApplicationType.ACCEPTANCE);
            applicationMonitor.informAuthor(group, pacilUser, type);
            applicationMonitor.informApplicant(group, pacilUser, type);
            group.getMembers().add(pacilUser);

        } else if (applicant.getStatus() == ApplicationType.NEW_APPLICANT && type == ApplicationType.REJECTION) {
            applicant.setStatus(ApplicationType.REJECTION);
            applicationMonitor.informAuthor(group, pacilUser, type);
            applicationMonitor.informApplicant(group, pacilUser, type);

        }
        group.removeApplication(applicant);
        applicantRepository.save(applicant);
        return applicant;
    }

    @Override
    public List<Application> getAllApplicationByGroup(int id) {
        var group = groupRepository.findById(id);
        return applicantRepository.findApplicantByPacilGroup(group);
    }

    @Override
    public List<Application> getAllActiveApplicationByGroup(int id) {
        var group = groupRepository.findById(id);
        return applicantRepository.findApplicantByPacilGroupAndStatus(group, ApplicationType.NEW_APPLICANT);
    }

    @Override
    public List<Application> getAllApplicationByUser(long npm) {
        var pacilUser = pacilUserRepository.findByNpm(npm);
        return applicantRepository.findApplicantByPacilUser(pacilUser);
    }

    @Override
    public List<Application> getAllActiveApplicationByUser(long npm) {
        var pacilUser = pacilUserRepository.findByNpm(npm);
        return applicantRepository.findApplicantByPacilUserAndStatus(pacilUser, ApplicationType.NEW_APPLICANT);
    }

}
