package cs.ui.pacilfindgroup.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import cs.ui.pacilfindgroup.dto.CourseDTO;
import cs.ui.pacilfindgroup.exceptions.AuthException;
import cs.ui.pacilfindgroup.exceptions.ResourceNotFoundException;
import cs.ui.pacilfindgroup.model.Course;
import cs.ui.pacilfindgroup.repository.CourseRepository;
import cs.ui.pacilfindgroup.repository.PacilUserRepository;
import cs.ui.pacilfindgroup.services.CourseServiceImpl;
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
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
class CourseControllerTest {

    private static String BASE_URL = "/api/v1/course";
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JwtUtility jwtUtility;

    @MockBean
    private CourseServiceImpl courseService;

    @MockBean
    private CourseRepository courseRepository;

    @MockBean
    private PacilUserRepository pacilUserRepository;

    private CourseDTO courseDTO;
    private Course course;

    @BeforeEach
    void setUp() {
        course = new Course();
        course.setCourseId("CSUI");
        course.setCourseName("Desain Algoritma");
        courseDTO = new CourseDTO();
        courseDTO.setCourseId("CSUI");
        courseDTO.setCourseName("Desain Algoritma");
        courseDTO.setUsername("hakim");
    }

    @Test
    void testcreateCourseSuccess() throws Exception {

        when(jwtUtility.getUsernameFromToken(any(String.class))).thenReturn("hakim");
        when(courseService.save(courseDTO)).thenReturn(course);
        RequestBuilder request = MockMvcRequestBuilders
                .post(BASE_URL + "/create-course")
                .header("Authorization", "Bearer jwttoken")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(courseDTO))
                .characterEncoding("utf-8")
                .accept(MediaType.APPLICATION_JSON);

        MvcResult response = mockMvc
                .perform(request)
                .andExpect(status().is(200))
                .andExpect(MockMvcResultMatchers.content().json("{\"courseId\":\"CSUI\",\"courseName\":\"Desain Algoritma\"}"))
                .andReturn();
    }

    @Test
    void testcreateCourseShouldReturnPermissionDeniedMessage() throws Exception {

        when(jwtUtility.getUsernameFromToken(any(String.class))).thenReturn("hakim");
        when(courseService.save(any(CourseDTO.class)))
                .thenThrow(new AuthException("Permission is denied, superuser only"));
        RequestBuilder request = MockMvcRequestBuilders
                .post(BASE_URL + "/create-course")
                .header("Authorization", "Bearer jwttoken")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(courseDTO))
                .characterEncoding("utf-8")
                .accept(MediaType.APPLICATION_JSON);

        MvcResult response = mockMvc
                .perform(request)
                .andExpect(status().is(403))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Permission is denied, superuser only"))
                .andReturn();
    }

    @Test
    void testcreateCourseShouldReturnIllegalArgumentMessage() throws Exception {

        when(jwtUtility.getUsernameFromToken(any(String.class))).thenReturn("hakim");
        when(courseService.save(any(CourseDTO.class)))
                .thenThrow(new IllegalArgumentException("courseId can't be blank"));
        RequestBuilder request = MockMvcRequestBuilders
                .post(BASE_URL + "/create-course")
                .header("Authorization", "Bearer jwttoken")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(courseDTO))
                .characterEncoding("utf-8")
                .accept(MediaType.APPLICATION_JSON);

        MvcResult response = mockMvc
                .perform(request)
                .andExpect(status().is(400))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message")
                        .value("courseId can't be blank"))
                .andReturn();
    }

    @Test
    void testFindCourseLikeShouldReturnListOfCourses() throws Exception {
        when(jwtUtility.getUsernameFromToken(any(String.class))).thenReturn("hakim");
        when(courseService.findByNameContains(any(String.class)))
                .thenReturn(List.of(course));
        RequestBuilder request = MockMvcRequestBuilders
                .get(BASE_URL + "/search")
                .header("Authorization", "Bearer jwttoken")
                .param("name", "daa")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(courseDTO))
                .characterEncoding("utf-8")
                .accept(MediaType.APPLICATION_JSON);

        MvcResult response = mockMvc
                .perform(request)
                .andExpect(status().is(200))
                .andExpect(content().json(" [{\"courseId\":\"CSUI\",\"courseName\":\"Desain Algoritma\"}]"))
                .andReturn();
    }

    @Test
    void testFindCourseLikeShouldReturnEmptyArray() throws Exception {
        when(courseService.findByNameContains(any(String.class)))
                .thenReturn(new ArrayList<>());
        RequestBuilder request = MockMvcRequestBuilders
                .get(BASE_URL + "/search")
                .header("Authorization", "Bearer jwttoken")
                .param("name", "daa")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(courseDTO))
                .characterEncoding("utf-8")
                .accept(MediaType.APPLICATION_JSON);

        MvcResult response = mockMvc
                .perform(request)
                .andExpect(status().is(200))
                .andExpect(content().json("[]"))
                .andReturn();
    }

    @Test
    void testDeleteByIdShouldReturnSuccessMessage() throws Exception {
        when(jwtUtility.getUsernameFromToken(any(String.class))).thenReturn("hakim");
        doNothing().when(courseService).deleteById(any(String.class), any(String.class));
        RequestBuilder request = MockMvcRequestBuilders
                .delete(BASE_URL + "/delete/{id}", 1)
                .header("Authorization", "Bearer jwttoken")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(courseDTO))
                .characterEncoding("utf-8")
                .accept(MediaType.APPLICATION_JSON);

        MvcResult response = mockMvc
                .perform(request)
                .andExpect(status().is(200))
                .andExpect(content()
                        .json("{\"status_code\":200,\"message\":\"course has been deleted\",\"ok\":true}"))
                .andReturn();
    }

    @Test
    void testDeleteByIdShouldReturnErrorMessage() throws Exception {
        when(jwtUtility.getUsernameFromToken(any(String.class))).thenReturn("hakim");
        doThrow(new ResourceNotFoundException("Course with 1 Not Found"))
                .when(courseService).deleteById(any(String.class), any(String.class));
        RequestBuilder request = MockMvcRequestBuilders
                .delete(BASE_URL + "/delete/{id}", 1)
                .header("Authorization", "Bearer jwttoken")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(courseDTO))
                .characterEncoding("utf-8")
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().is(404))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Course with 1 Not Found"))
                .andReturn();
    }

    @Test
    void testDeleteByIdFailForNonSuperuser() throws Exception {
        when(jwtUtility.getUsernameFromToken(any(String.class))).thenReturn("hakim");
        doThrow(new AuthException("Permission is denied, superuser only"))
                .when(courseService).deleteById(any(String.class), any(String.class));
        RequestBuilder request = MockMvcRequestBuilders
                .delete(BASE_URL + "/delete/{id}", 1)
                .header("Authorization", "Bearer jwttoken")
                .accept(MediaType.APPLICATION_JSON);


        MvcResult response = mockMvc
                .perform(request)
                .andExpect(status().is(403))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Permission is denied, superuser only"))
                .andReturn();
    }
}