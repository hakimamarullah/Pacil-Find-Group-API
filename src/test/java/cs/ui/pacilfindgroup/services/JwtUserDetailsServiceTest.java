package cs.ui.pacilfindgroup.services;

import cs.ui.pacilfindgroup.exceptions.UsernameNotFoundException;
import cs.ui.pacilfindgroup.model.PacilUser;
import cs.ui.pacilfindgroup.repository.PacilUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class JwtUserDetailsServiceTest {

    @Autowired
    private JwtUserDetailsService jwtUserDetailsService;

    @MockBean
    private PacilUserRepository pacilUserRepository;

    private PacilUser pacilUser;

    @BeforeEach
    void setUp(){
        pacilUser = new PacilUser();
        pacilUser.setNpm(1906293051L);
        pacilUser.setUsername("Halo");
        pacilUser.setSuperuser(true);
        pacilUser.setPassword("secret");

    }

    @Test
    void testloadUserByUsernameShouldReturnUserDetails() {
        when(pacilUserRepository.findByUsername(any(String.class))).thenReturn(pacilUser);

        var userDetails = jwtUserDetailsService.loadUserByUsername("Halo");
        assertNotNull(userDetails);
        assertEquals(User.class, userDetails.getClass());
        assertTrue(userDetails.getAuthorities()
                .contains(new SimpleGrantedAuthority("ROLE_SUPERUSER")));

    }

    @Test
    void testloadUserByUsernameNotFoundShouldThrowException() {
        when(pacilUserRepository.findByUsername(any(String.class))).thenReturn(null);

        UsernameNotFoundException ex = assertThrows(UsernameNotFoundException.class,
                ()-> jwtUserDetailsService.loadUserByUsername("Halo"));
        assertEquals("User not found with username: Halo",ex.getMessage());
        verify(pacilUserRepository, times(1))
                .findByUsername(any(String.class));
    }


    @Test
    void testIsSuperuserTrue() {
        when(pacilUserRepository.findByUsername(any(String.class))).thenReturn(pacilUser);

        var userDetails = jwtUserDetailsService.loadUserByUsername("Halo");

        assertTrue(jwtUserDetailsService.isSuperuser(userDetails));
    }

    @Test
    void testIsSuperuserFalse() {
        pacilUser.setSuperuser(false);
        when(pacilUserRepository.findByUsername(any(String.class))).thenReturn(pacilUser);

        var userDetails = jwtUserDetailsService.loadUserByUsername("Halo");

        assertFalse(jwtUserDetailsService.isSuperuser(userDetails));
    }
}