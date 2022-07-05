package cs.ui.pacilfindgroup.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "groups")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true, value = {"hibernateLazyInitializer", "handler"})
public class Group {

    @Id
    @Column(name = "group_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int groupId;


    @Column(name = "kelas", columnDefinition = "character varying(5) default 'N/A'")
    private String kelas;


    @Column(name = "completed", columnDefinition = "boolean default false")
    private boolean completed;


    @Column(name = "max_member", columnDefinition = "int default '5'")
    private int maxMember;

    @ManyToOne
    @JoinColumn(name = "npm")
    private PacilUser author;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    @OneToMany(mappedBy = "pacilGroup", orphanRemoval = true, cascade = CascadeType.ALL)
    @JsonIgnoreProperties("pacilGroup")
    private Set<Application> applications = new HashSet<>();

    @ManyToMany
    @JoinColumn(name = "group_id")
    private Set<PacilUser> members = new HashSet<>();

    @ElementCollection
    @CollectionTable(name = "groupLogs", joinColumns = @JoinColumn(name = "id"))
    @Column(name = "groupLogs")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<String> groupLogs = new ArrayList<>();

    @PreRemove
    private void removeGroupFromUser() {
        author.removeGroup(this);
        for (PacilUser user : members) {
            user.removeGroup(this);
        }
    }

    public void addApplication(Application application) {

        this.applications.add(application);
        if (this.applications.size() >= maxMember) {
            this.completed = true;
        }
    }

    public void removeApplication(Application application) {
        this.applications.remove(application);
    }

    public void setKelas(final String kelas) {
        this.kelas = Objects.requireNonNullElse(kelas, "N/A");
    }

    public void setMaxMember(final Integer maxMember) {
        this.maxMember = Objects.requireNonNullElse(maxMember, 5);
    }

    void handleNotification(PacilUser pacilUser, ApplicationType type) {
        if (type == ApplicationType.NEW_APPLICANT) {
            author.handleNotification(pacilUser, type);
        } else if (type == ApplicationType.ACCEPTANCE)
            groupLogs.add("Accepted a new member: " + pacilUser.getUsername());
    }
}
