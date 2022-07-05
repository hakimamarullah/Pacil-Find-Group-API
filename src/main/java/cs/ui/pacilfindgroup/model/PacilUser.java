package cs.ui.pacilfindgroup.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "pacil_user")
@JsonIgnoreProperties(ignoreUnknown = true, value = {"hibernateLazyInitializer", "handler"})
public class PacilUser {

    @Id
    @Column(name = "npm")
    private Long npm;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "lineID")
    private String lineID;

    @Column(name = "password")
    @JsonIgnore
    private String password;

    @Column(name = "isSuperuser", columnDefinition = "boolean default false")
    private boolean isSuperuser;

    @JsonIgnore
    @ManyToMany
    @JoinColumn(name = "npm")
    private Set<Group> groups = new HashSet<>();

    @OneToMany(mappedBy = "pacilUser", orphanRemoval = true, cascade = CascadeType.ALL)
    @JsonIgnoreProperties("pacilUser")
    private Set<Application> applications = new HashSet<>();

    @ElementCollection
    @CollectionTable(name = "pacilUserLogs", joinColumns = @JoinColumn(name = "id"))
    @Column(name = "pacilUserLogs")
    @JsonIgnore
    private List<String> pacilUserLogs =  new ArrayList<>();

    public void addGroup(final Group group) {
        this.groups.add(group);
    }

    void handleNotification(PacilUser pacilUser, ApplicationType type){
        if(type == ApplicationType.NEW_APPLICANT){
            pacilUserLogs.add("A new member wants to join your group: "+pacilUser);
        }
    }

    void handleNotification(Group pacilGroup, ApplicationType type){
        var handleLogs = "";
        if (type == ApplicationType.ACCEPTANCE) {
            handleLogs += "Accepted into";
        } else if (type == ApplicationType.REJECTION) {
            handleLogs += "Rejected from";
        } else if (type == ApplicationType.CANCEL) {
            handleLogs += "Deleted application request from";
        }
        handleLogs += " applied group with course: " + pacilGroup.getCourse().getCourseName() + " and class: " + pacilGroup.getKelas();
        pacilUserLogs.add(handleLogs);
    }

    public void removeGroup(Group group) {
        this.groups.remove(group);
    }
}
