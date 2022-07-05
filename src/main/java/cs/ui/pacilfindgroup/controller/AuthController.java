package cs.ui.pacilfindgroup.controller;

import cs.ui.pacilfindgroup.dto.JwtRequest;
import cs.ui.pacilfindgroup.dto.JwtResponse;
import cs.ui.pacilfindgroup.dto.UserDTO;
import cs.ui.pacilfindgroup.exceptions.ApiRequestException;
import cs.ui.pacilfindgroup.exceptions.AuthException;
import cs.ui.pacilfindgroup.model.PacilUser;
import cs.ui.pacilfindgroup.services.JwtUserDetailsService;
import cs.ui.pacilfindgroup.services.UserServiceImpl;
import cs.ui.pacilfindgroup.util.JwtUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    @Autowired
    @Qualifier("jwtuserdetailsservice")
    private JwtUserDetailsService jwtUserDetailsService;

    @Autowired
    @Qualifier("userserviceimpl")
    private UserServiceImpl userService;

    @Autowired
    private JwtUtility jwtUtility;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping(value = "/authenticate", produces = "application/json")
    public ResponseEntity<JwtResponse> login(@RequestBody final JwtRequest jwtRequest) {
        authenticate(jwtRequest.getUsername(), jwtRequest.getPassword());
        var userDetails = jwtUserDetailsService.loadUserByUsername(jwtRequest.getUsername());

        var token = jwtUtility.generateToken(userDetails);

        return ResponseEntity.ok(new JwtResponse(userDetails.getUsername(),
                jwtUserDetailsService.isSuperuser(userDetails),
                token));
    }

    @PostMapping(value = "/register", produces = "application/json")
    public ResponseEntity<PacilUser> register(@RequestBody final UserDTO userDTO) {
        return ResponseEntity.ok(userService.save(userDTO));
    }

    private void authenticate(final String username, final String password) throws ApiRequestException {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (final DisabledException e) {
            throw new AuthException("Disabled");
        } catch (final BadCredentialsException e) {
            throw new AuthException("INVALID_CREDENTIALS");
        }
    }
}
