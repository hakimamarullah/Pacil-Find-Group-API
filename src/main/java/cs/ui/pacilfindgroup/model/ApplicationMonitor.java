package cs.ui.pacilfindgroup.model;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ApplicationMonitor implements EventMonitor {

    private static ApplicationMonitor applicationMonitor;

    public static synchronized ApplicationMonitor getApplicationMonitor() {
        if (applicationMonitor == null) {
            applicationMonitor = new ApplicationMonitor();
        }
        return applicationMonitor;
    }

    @Override
    public void informAuthor(Group group, PacilUser pacilUser, ApplicationType type) {
        group.handleNotification(pacilUser, type);
    }

    @Override
    public void informApplicant(Group group, PacilUser pacilUser, ApplicationType type) {
        pacilUser.handleNotification(group, type);
    }
}
