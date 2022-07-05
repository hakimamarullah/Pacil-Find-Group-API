package cs.ui.pacilfindgroup.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import cs.ui.pacilfindgroup.dto.UserDTO;
import cs.ui.pacilfindgroup.model.PacilUser;
import cs.ui.pacilfindgroup.repository.PacilUserRepository;
import cs.ui.pacilfindgroup.services.JwtUserDetailsService;
import cs.ui.pacilfindgroup.services.UserServiceImpl;
import cs.ui.pacilfindgroup.util.JwtUtility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {
    private static String BASE_URL = "/api/v1/auth";
    @Autowired
    private MockMvc mockMvc;


    @MockBean
    private JwtUserDetailsService jwtUserDetailsService;

    @MockBean
    private UserServiceImpl userService;

    @MockBean
    private PacilUserRepository pacilUserRepository;

    @MockBean
    private JwtUtility jwtUtility;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private PasswordEncoder passwordEncoder;

    private PacilUser pacilUser;
    private UserDTO userDTO;

    @BeforeEach
    void setUp() {
        pacilUser = new PacilUser();
        userDTO = new UserDTO();
        pacilUser.setNpm(1906293051L);
        pacilUser.setUsername("Hakim");
        pacilUser.setPassword("Hai");
        pacilUser.setSuperuser(true);
        pacilUser.setEmail("hakim@ui.ac.id");
        pacilUser.setLineID("line");

        userDTO.setNpm(1906293051L);
        userDTO.setEmail("test@ui.ac.id");
        userDTO.setSuperuser(true);
        userDTO.setLineID("halo_line");
        userDTO.setPassword("secret");

    }

    @Test
    void login() throws Exception {

        UserDetails user = new User(pacilUser.getUsername(), pacilUser.getPassword(), new ArrayList<>());
        when(jwtUserDetailsService.loadUserByUsername(any(String.class)))
                .thenReturn(user);

        when(jwtUserDetailsService.isSuperuser(user)).thenReturn(true);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(new UsernamePasswordAuthenticationToken(pacilUser.getUsername(),
                        pacilUser.getPassword()));

        when(pacilUserRepository.findByUsername(any(String.class))).thenReturn(null);

        when(jwtUtility.generateToken(user)).thenReturn("ABCD");

        RequestBuilder request = MockMvcRequestBuilders
                .post(BASE_URL + "/authenticate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(pacilUser))
                .accept(MediaType.APPLICATION_JSON);

        MvcResult response = mockMvc
                .perform(request)
                .andExpect(status().is(200))
                .andExpect(content()
                        .json("{\"username\":\"Hakim\",\"jwtToken\":\"ABCD\",\"superuser\":true}", false))
                .andReturn();

    }

    @Test
    void testLoginFailedDisabledException() throws Exception {

        UserDetails user = new User(pacilUser.getUsername(), pacilUser.getPassword(), new ArrayList<>());
        when(jwtUserDetailsService.loadUserByUsername(any(String.class)))
                .thenReturn(user);

        when(jwtUserDetailsService.isSuperuser(user)).thenReturn(true);


        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new DisabledException(""));

        when(pacilUserRepository.findByUsername(any(String.class))).thenReturn(null);

        when(jwtUtility.generateToken(user)).thenReturn("ABCD");

        RequestBuilder request = MockMvcRequestBuilders
                .post(BASE_URL + "/authenticate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(pacilUser))
                .accept(MediaType.APPLICATION_JSON);

        MvcResult response = mockMvc
                .perform(request)
                .andExpect(status().is(403))
                .andExpect(jsonPath("$.message").value("Disabled"))
                .andReturn();

    }

    @Test
    void testLoginFailedBadCredential() throws Exception {

        UserDetails user = new User(pacilUser.getUsername(), pacilUser.getPassword(), new ArrayList<>());
        when(jwtUserDetailsService.loadUserByUsername(any(String.class)))
                .thenReturn(user);

        when(jwtUserDetailsService.isSuperuser(user)).thenReturn(true);


        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException(""));

        when(pacilUserRepository.findByUsername(any(String.class))).thenReturn(null);

        when(jwtUtility.generateToken(user)).thenReturn("ABCD");

        RequestBuilder request = MockMvcRequestBuilders
                .post(BASE_URL + "/authenticate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(pacilUser))
                .accept(MediaType.APPLICATION_JSON);

        MvcResult response = mockMvc
                .perform(request)
                .andExpect(status().is(403))
                .andExpect(jsonPath("$.message").value("INVALID_CREDENTIALS"))
                .andReturn();

    }

    @Test
    void testRegisterWithValidDataShouldReturnPacilUserJsonObject() throws Exception {
        UserDetails user = new User(pacilUser.getUsername(), pacilUser.getPassword(), new ArrayList<>());
        when(passwordEncoder.encode(any(String.class))).thenReturn("ab76%ad@");
        when(userService.save(any(UserDTO.class))).thenReturn(pacilUser);
        when(pacilUserRepository.save(any(PacilUser.class))).thenReturn(pacilUser);


        RequestBuilder request = MockMvcRequestBuilders
                .post(BASE_URL + "/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(userDTO))
                .characterEncoding("utf-8")
                .accept(MediaType.APPLICATION_JSON);

        String expected = "{\"npm\":1906293051,\"email\":\"hakim@ui.ac.id\",\"username\":\"Hakim\",\"lineID\":\"line\",\"applications\":[],\"superuser\":true}";
        MvcResult response = mockMvc
                .perform(request)
                .andExpect(status().is(200))
                .andExpect(content()
                        .json(expected, false))
                .andReturn();
    }

    @Test
    void testRegisterWithValidDataShouldReturnErrorMessage() throws Exception {
        userDTO.setEmail("hakim@gmail.com@ui.ac.id");
        when(passwordEncoder.encode(any(String.class))).thenReturn("ab76%ad@");
        when(userService.save(userDTO)).thenCallRealMethod();
        when(pacilUserRepository.save(any(PacilUser.class))).thenReturn(pacilUser);


        RequestBuilder request = MockMvcRequestBuilders
                .post(BASE_URL + "/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(userDTO))
                .accept(MediaType.APPLICATION_JSON);

        String expected = "{\"message\":\"Only email with domain ui.ac.id is acceptable\",\"error\":\"BAD_REQUEST\",\"date\":\"2022-05-15T03:47:34.897+00:00\",\"statusCode\":400}";
        MvcResult response = mockMvc
                .perform(request)
                .andExpect(status().is(400))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Only email with domain ui.ac.id is acceptable"))
                .andReturn();

    }
}