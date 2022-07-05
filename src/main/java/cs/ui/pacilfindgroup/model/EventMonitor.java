package cs.ui.pacilfindgroup.model;

public interface EventMonitor {
    void informAuthor(Group group, PacilUser pacilUser, ApplicationType type);
    void informApplicant(Group group, PacilUser pacilUser, ApplicationType type);
}
