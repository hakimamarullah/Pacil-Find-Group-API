package cs.ui.pacilfindgroup.controller;

import cs.ui.pacilfindgroup.dto.GroupRequest;
import cs.ui.pacilfindgroup.model.Group;
import cs.ui.pacilfindgroup.services.GroupServiceImpl;
import cs.ui.pacilfindgroup.util.JwtUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/group")
public class GroupController {
    @Autowired
    private GroupServiceImpl groupService;

    @Autowired
    @Qualifier("jwtutility")
    private JwtUtility jwtUtility;

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Group>> findAll() {
        return ResponseEntity.ok(groupService.findAll());
    }

    @PostMapping(path = "/create-group")
    public ResponseEntity<Group> createGroup(@RequestHeader("Authorization") final String jwt,
                                             @RequestBody final GroupRequest groupRequest) {
        groupRequest.setUsername(jwtUtility.getUsernameFromToken(jwt.substring(7)));
        return ResponseEntity.ok(groupService.createGroup(groupRequest));
    }

    @DeleteMapping(path = "/delete-group/{id}")
    public ResponseEntity<Map<String, Object>> deleteGroupById(@PathVariable("id") int id) {
        this.groupService.deleteById(id);
        Map<String, Object> res = new HashMap<>();
        res.put("deleted", true);
        return ResponseEntity.ok(res);
    }

    @GetMapping(path = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Group>> getAllGroupByCourseName(@RequestParam("courseName")
                                                                       String courseName) {
        return ResponseEntity.ok().body(this.groupService.findAllByCourseName(courseName));
    }

    @PutMapping(path = "/close/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> closeRecruitment(@PathVariable("id") int id) {
        Map<String, Object> response = new HashMap<>();
        response.put("ok", true);
        response.put("message", "group recruitment has been closed");
        this.groupService.closeRecruitmentGroupById(id);
        return ResponseEntity.ok(response);
    }

}
