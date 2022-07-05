package cs.ui.pacilfindgroup.controller;

import cs.ui.pacilfindgroup.exceptions.UsernameNotFoundException;
import cs.ui.pacilfindgroup.model.PacilUser;
import cs.ui.pacilfindgroup.services.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {
    private static String BASE_URL = "/api/v1/user";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserServiceImpl userService;

    private PacilUser user;

    @BeforeEach
    void setUp() {
        user = new PacilUser();
        user.setUsername("test");
        user.setNpm(1906293051L);
        user.setEmail("test@ui.ac.id");
        user.setSuperuser(true);
        user.setLineID("halo_line");
        user.setPassword("secret");
    }

    @Test
    void testGetUserDetailByUsernameSuccess() throws Exception {
        when(userService.getUserByUsername(any(String.class))).thenReturn(user);
        RequestBuilder request = MockMvcRequestBuilders
                .get(BASE_URL + "/detail")
                .header("Authorization", "Bearer jwttoken")
                .param("username", "hakim")
                .accept(MediaType.APPLICATION_JSON);

        String expected = "{\"npm\":1906293051,\"email\":\"test@ui.ac.id\",\"username\":\"test\",\"lineID\":\"halo_line\",\"applications\":[],\"superuser\":true}";
        MvcResult response = mockMvc
                .perform(request)
                .andExpect(status().is(200))
                .andExpect(content().json(expected))
                .andReturn();
    }

    @Test
    void testGetUserDetailByUsernameFailed() throws Exception {
        when(userService.getUserByUsername(any(String.class)))
                .thenThrow(new UsernameNotFoundException(String.format("User %s Not Found", user.getUsername())));
        RequestBuilder request = MockMvcRequestBuilders
                .get(BASE_URL + "/detail")
                .header("Authorization", "Bearer jwttoken")
                .param("username", "hakim")
                .accept(MediaType.APPLICATION_JSON);

        String expected = "User " + user.getUsername() + " Not Found";
        MvcResult response = mockMvc
                .perform(request)
                .andExpect(status().is(404))
                .andExpect(jsonPath("$.message").value(expected))
                .andReturn();
    }

    @Test
    void testGetAllGroup() throws Exception {
        when(userService.getAllGroup(anyString())).thenReturn(user.getGroups());
        RequestBuilder request = MockMvcRequestBuilders
                .get(BASE_URL + "/all-recruitments")
                .header("Authorization", "Bearer jwttoken")
                .param("username", "hakim")
                .accept(MediaType.APPLICATION_JSON);

        String expected = "[]";
        MvcResult response = mockMvc
                .perform(request)
                .andExpect(status().is(200))
                .andExpect(content().json(expected))
                .andReturn();
    }

    @Test
    void testGetAllGroupFailed() throws Exception {
        when(userService.getAllGroup(any(String.class)))
                .thenThrow(new UsernameNotFoundException(String.format("User %s Not Found", user.getUsername())));
        RequestBuilder request = MockMvcRequestBuilders
                .get(BASE_URL + "/all-recruitments")
                .header("Authorization", "Bearer jwttoken")
                .param("username", "hakim")
                .accept(MediaType.APPLICATION_JSON);

        String expected = "User " + user.getUsername() + " Not Found";
        MvcResult response = mockMvc
                .perform(request)
                .andExpect(status().is(404))
                .andExpect(jsonPath("$.message").value(expected))
                .andReturn();
    }
}