package cs.ui.pacilfindgroup.repository;

import cs.ui.pacilfindgroup.model.PacilUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;


@RunWith(SpringRunner.class)
@DataJpaTest
class PacilUserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private PacilUserRepository pacilUserRepository;

    private PacilUser pacilUser;

    @BeforeEach
    void setUp(){
        pacilUser = new PacilUser();
        pacilUser.setNpm(1906293051L);
        pacilUser.setUsername("Hakim");
        pacilUser.setPassword("Hai");
        pacilUser.setSuperuser(true);
        pacilUser.setEmail("hakim@ui.ac.id");
        pacilUser.setLineID("line");

        entityManager.persistAndFlush(pacilUser);
    }
    @Test
    void findByUsername() {
        var user = pacilUserRepository.findByUsername("Hakim");

        assertNotNull(user);
        assertEquals("Hai", pacilUser.getPassword());
    }

    @Test
    void findByNpm() {
        var user = pacilUserRepository.findByNpm(1906293051L);

        assertNotNull(user);
        assertEquals("Hai", pacilUser.getPassword());

    }

    @Test
    void findByEmail() {
        var user = pacilUserRepository.findByEmail("hakim@ui.ac.id");

        assertNotNull(user);
        assertEquals("Hai", pacilUser.getPassword());
    }

    @Test
    void existsPacilUsersByEmail() {
        var user = pacilUserRepository.existsPacilUsersByEmail("hakim@ui.ac.id");

        assertTrue(user);
    }

    @Test
    void existsPacilUsersByNpm() {
        var user = pacilUserRepository.existsPacilUsersByNpm(1906293051L);

        assertTrue(user);
    }

    @Test
    void existsPacilUsersByUsernameAndIsSuperuserTrue() {
        var user = pacilUserRepository.existsPacilUsersByUsernameAndIsSuperuserTrue("Hakim");

        assertTrue(user);
    }
}