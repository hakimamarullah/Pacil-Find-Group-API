package cs.ui.pacilfindgroup.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import cs.ui.pacilfindgroup.dto.GroupRequest;
import cs.ui.pacilfindgroup.exceptions.DuplicateRecordException;
import cs.ui.pacilfindgroup.exceptions.ResourceNotFoundException;
import cs.ui.pacilfindgroup.model.Course;
import cs.ui.pacilfindgroup.model.Group;
import cs.ui.pacilfindgroup.model.PacilUser;
import cs.ui.pacilfindgroup.services.GroupServiceImpl;
import cs.ui.pacilfindgroup.util.JwtUtility;
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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
class GroupControllerTest {

    private static String BASE_URL = "/api/v1/group";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GroupServiceImpl groupService;

    @MockBean
    private JwtUtility jwtUtility;

    private Group group;
    private PacilUser pacilUser;
    private Course course;
    private GroupRequest groupRequest;

    @BeforeEach
    void setUp() {
        group = new Group();
        pacilUser = new PacilUser();
        groupRequest = new GroupRequest("hakim", "CSUI", "A", 10);
        course = new Course("CSUI", "DESAIN", new HashSet<>());

        pacilUser.setNpm(1906293051L);
        group.setCompleted(false);
        group.setGroupId(1);
        group.setAuthor(pacilUser);
        group.setCourse(course);
    }

    @Test
    void testFindAllShouldReturnListOfGroups() throws Exception {
        when(groupService.findAll()).thenReturn(List.of(group));

        RequestBuilder request = MockMvcRequestBuilders
                .get(BASE_URL + "/all")
                .header("Authorization", "Bearer jwttoken")
                .accept(MediaType.APPLICATION_JSON);
        String expected = "[{\"groupId\":1,\"kelas\":null,\"completed\":false,\"maxMember\":0,\"author\":{\"npm\":1906293051,\"email\":null,\"username\":null,\"lineID\":null,\"applications\":[],\"superuser\":false},\"course\":{\"courseId\":\"CSUI\",\"courseName\":\"DESAIN\"},\"applications\":[],\"members\":[]}]";
        MvcResult response = mockMvc
                .perform(request)
                .andExpect(status().is(200))
                .andExpect(MockMvcResultMatchers.content().json(expected))
                .andReturn();
    }

    @Test
    void testFindAllShouldEmptyList() throws Exception {
        when(groupService.findAll()).thenReturn(new ArrayList<>());

        RequestBuilder request = MockMvcRequestBuilders
                .get(BASE_URL + "/all")
                .header("Authorization", "Bearer jwttoken")
                .accept(MediaType.APPLICATION_JSON);
        String expected = "[]";
        MvcResult response = mockMvc
                .perform(request)
                .andExpect(status().is(200))
                .andExpect(MockMvcResultMatchers.content().json(expected))
                .andReturn();
    }


    @Test
    void testCreateGroupSuccess() throws Exception {
        when(jwtUtility.getUsernameFromToken(any(String.class))).thenReturn("");
        when(groupService.createGroup(any(GroupRequest.class))).thenReturn(group);

        RequestBuilder request = MockMvcRequestBuilders
                .post(BASE_URL + "/create-group")
                .header("Authorization", "Bearer jwttoken")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(groupRequest))
                .characterEncoding("utf-8")
                .accept(MediaType.APPLICATION_JSON);
        String expected = "{\"groupId\":1,\"kelas\":null,\"completed\":false,\"maxMember\":0,\"author\":{\"npm\":1906293051,\"email\":null,\"username\":null,\"lineID\":null,\"applications\":[],\"superuser\":false},\"course\":{\"courseId\":\"CSUI\",\"courseName\":\"DESAIN\"},\"applications\":[],\"members\":[]}";
        MvcResult response = mockMvc
                .perform(request)
                .andExpect(status().is(200))
                .andExpect(MockMvcResultMatchers.content().json(expected))
                .andReturn();
    }

    @Test
    void testCreateGroupShouldReturnResourceNotFoundMessage() throws Exception {
        when(jwtUtility.getUsernameFromToken(any(String.class))).thenReturn("");
        when(groupService.createGroup(any(GroupRequest.class))).thenThrow(new ResourceNotFoundException("Course with ID:" + groupRequest.getCourseId() + " Not Found"));

        RequestBuilder request = MockMvcRequestBuilders
                .post(BASE_URL + "/create-group")
                .header("Authorization", "Bearer jwttoken")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(groupRequest))
                .characterEncoding("utf-8")
                .accept(MediaType.APPLICATION_JSON);
        String expected = String.format("Course with ID:%s Not Found", groupRequest.getCourseId());
        MvcResult response = mockMvc
                .perform(request)
                .andExpect(status().is(404))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(expected))
                .andReturn();
    }

    @Test
    void testCreateGroupShouldReturnDuplicateErrorMessage() throws Exception {
        when(jwtUtility.getUsernameFromToken(any(String.class))).thenReturn("");
        when(groupService.createGroup(any(GroupRequest.class)))
                .thenThrow(new DuplicateRecordException("You have created a group for this course"));

        RequestBuilder request = MockMvcRequestBuilders
                .post(BASE_URL + "/create-group")
                .header("Authorization", "Bearer jwttoken")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(groupRequest))
                .characterEncoding("utf-8")
                .accept(MediaType.APPLICATION_JSON);
        String expected = "You have created a group for this course";
        MvcResult response = mockMvc
                .perform(request)
                .andExpect(status().is(409))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(expected))
                .andReturn();
    }

    @Test
    void testDeleteGroupById() throws Exception {
        when(jwtUtility.getUsernameFromToken(any(String.class))).thenReturn("hakim");
        RequestBuilder request = MockMvcRequestBuilders
                .delete(BASE_URL + "/delete-group/{id}", 1)
                .header("Authorization", "Bearer jwttoken")
                .accept(MediaType.APPLICATION_JSON);


        MvcResult response = mockMvc
                .perform(request)
                .andExpect(status().is(200))
                .andExpect(content().json("{\"deleted\":true}"))
                .andReturn();
    }

    @Test
    void testGetAllGroupByCourseName() throws Exception {
        when(groupService.findAllByCourseName(any(String.class))).thenReturn(List.of(group));
        RequestBuilder request = MockMvcRequestBuilders
                .get(BASE_URL + "/search")
                .header("Authorization", "Bearer jwttoken")
                .param("courseName", "desain")
                .accept(MediaType.APPLICATION_JSON);

        String expected = "[{\"groupId\":1,\"kelas\":null,\"completed\":false,\"maxMember\":0,\"author\":{\"npm\":1906293051,\"email\":null,\"username\":null,\"lineID\":null,\"applications\":[],\"superuser\":false},\"course\":{\"courseId\":\"CSUI\",\"courseName\":\"DESAIN\"},\"applications\":[],\"members\":[]}]";
        MvcResult response = mockMvc
                .perform(request)
                .andExpect(status().is(200))
                .andExpect(MockMvcResultMatchers.content().json(expected))
                .andReturn();
    }

    @Test
    void testGetAllGroupByCourseNameShouldReturnEmptyList() throws Exception {
        when(groupService.findAllByCourseName(any(String.class))).thenReturn(new ArrayList<>());
        RequestBuilder request = MockMvcRequestBuilders
                .get(BASE_URL + "/search")
                .header("Authorization", "Bearer jwttoken")
                .param("courseName", "desain")
                .accept(MediaType.APPLICATION_JSON);

        String expected = "[]";
        MvcResult response = mockMvc
                .perform(request)
                .andExpect(status().is(200))
                .andExpect(MockMvcResultMatchers.content().json(expected))
                .andReturn();
    }

    @Test
    void testCloseRecruitmentSuccess() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .put(BASE_URL + "/close/{id}", 1)
                .header("Authorization", "Bearer jwttoken")
                .accept(MediaType.APPLICATION_JSON);

        String expected = "{\"ok\":true,\"message\":\"group recruitment has been closed\"}";
        MvcResult response = mockMvc
                .perform(request)
                .andExpect(status().is(200))
                .andExpect(MockMvcResultMatchers.content().json(expected))
                .andReturn();
    }

    @Test
    void testCloseRecruitmentFailedGroupIdNotFound() throws Exception {
        doThrow(new ResourceNotFoundException("Group not found with ID: " + group.getGroupId()))
                .when(groupService).closeRecruitmentGroupById(anyInt());
        RequestBuilder request = MockMvcRequestBuilders
                .put(BASE_URL + "/close/{id}", group.getGroupId())
                .header("Authorization", "Bearer jwttoken")
                .accept(MediaType.APPLICATION_JSON);

        String expected = "Group not found with ID: 1";
        MvcResult response = mockMvc
                .perform(request)
                .andExpect(status().is(404))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(expected))
                .andReturn();
    }
}