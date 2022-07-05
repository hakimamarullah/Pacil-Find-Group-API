package cs.ui.pacilfindgroup.controller;

import cs.ui.pacilfindgroup.dto.AcceptorDeclineApplicantDTO;
import cs.ui.pacilfindgroup.dto.ApplicationDTO;
import cs.ui.pacilfindgroup.model.Application;
import cs.ui.pacilfindgroup.services.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/application")
public class ApplicationController {

    @Autowired
    private ApplicationService applicationService;

    @GetMapping(path = "/pacil_group_active/{id}", produces = {"application/json"})
    @ResponseBody
    public ResponseEntity<List<Application>> getAllActiveApplicantByGroup(@PathVariable(value = "id") int id) {
        return ResponseEntity.ok(applicationService.getAllActiveApplicationByGroup(id));
    }

    @GetMapping(path = "/pacil_group/{id}", produces = {"application/json"})
    @ResponseBody
    public ResponseEntity<List<Application>> getAllApplicantByGroup(@PathVariable(value = "id") int id) {
        return ResponseEntity.ok(applicationService.getAllApplicationByGroup(id));
    }

    @PutMapping(path = "/applicant/{id}", produces = {"application/json"})
    @ResponseBody
    public ResponseEntity<Application> acceptOrDeclineApplicant(@PathVariable(value = "id") long id, @RequestBody AcceptorDeclineApplicantDTO typeId) {
        return ResponseEntity.ok(applicationService.acceptOrDeclineApplicant(id, typeId));
    }

    @GetMapping(path = "/pacil_user_active/{id}", produces = {"application/json"})
    @ResponseBody
    public ResponseEntity<List<Application>> getAllActiveApplicantByUser(@PathVariable(value = "id") long npm) {
        return ResponseEntity.ok(applicationService.getAllActiveApplicationByUser(npm));
    }

    @GetMapping(path = "/pacil_user/{id}", produces = {"application/json"})
    @ResponseBody
    public ResponseEntity<List<Application>> getAllApplicantByUser(@PathVariable(value = "id") long npm) {
        return ResponseEntity.ok(applicationService.getAllApplicationByUser(npm));
    }

    @PostMapping(path = "/send_application", produces = {"application/json"})
    @ResponseBody
    public ResponseEntity<Application> postApplicantByUser(@RequestBody ApplicationDTO application) {
        return ResponseEntity.ok(applicationService.createApplication(application));
    }

    @DeleteMapping(path = "/del_application/{id}", produces = {"application/json"})
    @ResponseBody
    public ResponseEntity<Application> delApplicantByUser(@PathVariable(value = "id") long id) {
        return ResponseEntity.ok(applicationService.deleteApplication(id));
    }
}
