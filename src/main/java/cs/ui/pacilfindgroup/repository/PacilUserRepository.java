package cs.ui.pacilfindgroup.repository;

import cs.ui.pacilfindgroup.model.PacilUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PacilUserRepository extends JpaRepository<PacilUser, Long> {
    PacilUser findByUsername(String username);

    PacilUser findByNpm(Long npm);

    PacilUser findByEmail(String email);

    boolean existsPacilUsersByEmail(String email);

    boolean existsPacilUsersByNpm(Long npm);

    boolean existsPacilUsersByUsernameAndIsSuperuserTrue(String username);
}
