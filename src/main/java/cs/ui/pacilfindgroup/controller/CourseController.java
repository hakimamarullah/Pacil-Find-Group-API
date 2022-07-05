package cs.ui.pacilfindgroup.controller;

import cs.ui.pacilfindgroup.dto.CourseDTO;
import cs.ui.pacilfindgroup.model.Course;
import cs.ui.pacilfindgroup.services.CourseServiceImpl;
import cs.ui.pacilfindgroup.util.JwtUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/course")
public class CourseController {
    @Autowired
    private CourseServiceImpl courseService;

    @Autowired
    @Qualifier("jwtutility")
    private JwtUtility jwtUtility;

    @PostMapping(path = "/create-course", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Course> createCourse(@RequestBody final CourseDTO course,
                                               @RequestHeader("Authorization") final String jwt) {
        course.setUsername(jwtUtility.getUsernameFromToken(jwt.substring(7)));
        return ResponseEntity.ok(courseService.save(course));
    }

    @GetMapping(path = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Course>> findCourseLike(@RequestParam("name") final String name) {
        return ResponseEntity.ok(courseService.findByNameContains(name));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Map<String, Object>> deleteById(@PathVariable("id") final String id,
                                                          @RequestHeader("Authorization") final String jwt) {
        final Map<String, Object> res = new HashMap<>();
        res.put("message", "course has been deleted");
        res.put("ok", true);
        res.put("status_code", HttpStatus.OK.value());

        var username = jwtUtility.getUsernameFromToken(jwt.substring(7));
        courseService.deleteById(id, username);
        return ResponseEntity.ok(res);
    }
}
