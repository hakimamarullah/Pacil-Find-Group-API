package cs.ui.pacilfindgroup.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
@Table(name = "Application")
@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true, value = {"hibernateLazyInitializer", "handler"})
public class Application {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "status")
    private ApplicationType status;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "paciluser_npm", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnoreProperties("applications")
    private PacilUser pacilUser;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "pacilgroup_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Group pacilGroup;

    public Application(PacilUser pacilUser, Group pacilGroup) {
        this.pacilUser = pacilUser;
        this.pacilGroup = pacilGroup;
        this.status = ApplicationType.NEW_APPLICANT;
    }
}
