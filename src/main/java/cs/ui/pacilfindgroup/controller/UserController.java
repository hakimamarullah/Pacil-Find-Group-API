package cs.ui.pacilfindgroup.controller;

import cs.ui.pacilfindgroup.model.Group;
import cs.ui.pacilfindgroup.model.PacilUser;
import cs.ui.pacilfindgroup.services.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {
    @Autowired
    @Qualifier("userserviceimpl")
    private UserServiceImpl userService;

    @GetMapping("/detail")
    public ResponseEntity<PacilUser> getUserDetailByUsername(@RequestParam("username") final String username) {
        return ResponseEntity.ok().body(this.userService.getUserByUsername(username));
    }

    @GetMapping("/all-recruitments")
    public ResponseEntity<Set<Group>> getAllGroup(@RequestParam("username") final String username) {
        return ResponseEntity.ok().body(this.userService.getAllGroup(username));
    }

}
