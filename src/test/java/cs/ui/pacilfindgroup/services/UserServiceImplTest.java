package cs.ui.pacilfindgroup.services;

import cs.ui.pacilfindgroup.dto.UserDTO;
import cs.ui.pacilfindgroup.exceptions.AuthException;
import cs.ui.pacilfindgroup.exceptions.EmailInvalidFormatException;
import cs.ui.pacilfindgroup.exceptions.UsernameNotFoundException;
import cs.ui.pacilfindgroup.model.PacilUser;
import cs.ui.pacilfindgroup.repository.GroupRepository;
import cs.ui.pacilfindgroup.repository.PacilUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
class UserServiceImplTest {

    @Autowired
    private UserServiceImpl userService;

    @MockBean
    private PacilUserRepository pacilUserRepository;




    private PacilUser user;
    private UserDTO userDTO;

    @BeforeEach
    void setup() {
        user = new PacilUser();
        userDTO = new UserDTO();
        user.setUsername("test");
        user.setNpm(1906293051L);
        user.setEmail("test@ui.ac.id");
        user.setSuperuser(true);
        user.setLineID("halo_line");
        user.setPassword("secret");
        user.setGroups(new HashSet<>());

        userDTO.setNpm(1906293051L);
        userDTO.setEmail("test@ui.ac.id");
        userDTO.setSuperuser(true);
        userDTO.setLineID("halo_line");
        userDTO.setPassword("secret");
    }

    @Test
    void testSaveUserWithValidCredentialData() {
        when(this.pacilUserRepository.save(any(PacilUser.class))).thenReturn(this.user);


        final PacilUser savedUser = this.userService.save(this.userDTO);
        assertEquals(this.user.getUsername(), savedUser.getUsername());
        assertEquals(this.user.getPassword(), savedUser.getPassword());
        assertEquals(this.user.getEmail(), savedUser.getEmail());
    }

    @Test
    void testSaveUserWithInvalidEmailData() {
        this.userDTO.setEmail("hakim@gmail.com");

        when(this.pacilUserRepository.save(any(PacilUser.class))).thenReturn(this.user);


        assertThrows(EmailInvalidFormatException.class, () -> this.userService.save(this.userDTO));
    }

    @Test
    void testSaveUserWithInvalidNPMData() {
        this.userDTO.setNpm(10_101_010_101L);

        when(this.pacilUserRepository.save(any(PacilUser.class))).thenReturn(this.user);


        assertThrows(AuthException.class, () -> this.userService.save(this.userDTO));
    }

    @Test
    void testSaveUserWithEmailExistShouldThrowException(){
        when(pacilUserRepository.existsPacilUsersByEmail(anyString())).thenReturn(true);

        assertThrows(AuthException.class,()->userService.save(userDTO));
    }

    @Test
    void testSaveUserWithNpmExistShouldThrowException(){
        when(pacilUserRepository.existsPacilUsersByNpm(any(Long.class))).thenReturn(true);

        assertThrows(AuthException.class,()->userService.save(userDTO));
    }

    @Test
    void testGetByUsernameReturnNonNull() {
        when(this.pacilUserRepository.findByUsername(anyString())).thenReturn(this.user);

        final PacilUser user = this.userService.getUserByUsername("test");
        assertNotNull(user);
        assertEquals("test", user.getUsername());
    }

    @Test
    void testGetByUsernameThrowException() {
        when(this.pacilUserRepository.findByUsername(anyString())).thenReturn(null);

        assertThrows(UsernameNotFoundException.class, () -> this.userService.getUserByUsername("test"));
    }

    @Test
    void testGetAllGroupSuccess(){
        when(pacilUserRepository.findByUsername(anyString())).thenReturn(user);

        assertEquals(0, userService.getAllGroup("hakim").size());
    }

    @Test
    void testGetAllGroupThrowUsernameNotFound(){
        when(pacilUserRepository.findByUsername(anyString())).thenReturn(null);

        assertThrows(UsernameNotFoundException.class,()-> userService.getAllGroup("hakim"));
    }
}