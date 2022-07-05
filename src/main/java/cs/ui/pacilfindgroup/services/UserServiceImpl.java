package cs.ui.pacilfindgroup.services;

import cs.ui.pacilfindgroup.dto.UserDTO;
import cs.ui.pacilfindgroup.exceptions.ApiRequestException;
import cs.ui.pacilfindgroup.exceptions.AuthException;
import cs.ui.pacilfindgroup.exceptions.EmailInvalidFormatException;
import cs.ui.pacilfindgroup.exceptions.UsernameNotFoundException;
import cs.ui.pacilfindgroup.model.Group;
import cs.ui.pacilfindgroup.model.PacilUser;
import cs.ui.pacilfindgroup.repository.PacilUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.regex.Pattern;

@Service("userserviceimpl")
public class UserServiceImpl implements UserService {
    @Autowired
    private PacilUserRepository pacilUserRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public PacilUser save(UserDTO user) throws ApiRequestException {
        var newUser = new PacilUser();
        var emailPattern = Pattern.compile("^[A-Za-z0-9+_.-]+@ui.ac.id$");
        if (!emailPattern.matcher(user.getEmail()).matches()) {
            throw new EmailInvalidFormatException("Only email with domain ui.ac.id is acceptable");
        }
        checkConstraintsEmailAndNPM(user.getEmail(), user.getNpm());

        newUser.setNpm(user.getNpm());
        newUser.setUsername(user.getEmail().split("@")[0]);
        newUser.setPassword(passwordEncoder.encode(user.getPassword()));
        newUser.setEmail(user.getEmail());
        newUser.setLineID(user.getLineID());
        newUser.setSuperuser(user.isSuperuser());
        return pacilUserRepository.save(newUser);
    }

    @Override
    public PacilUser getUserByUsername(String username) {
        var user = pacilUserRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(String.format("User %s Not Found", username));
        }
        return user;
    }

    @Override
    public Set<Group> getAllGroup(String username) {
        var user = pacilUserRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(String.format("User %s Not Found", username));
        }
        return user.getGroups();
    }

    private void checkConstraintsEmailAndNPM(String email, Long npm) throws ApiRequestException {
        if (pacilUserRepository.existsPacilUsersByEmail(email))
            throw new AuthException("Email already in use");
        if (pacilUserRepository.existsPacilUsersByNpm(npm))
            throw new AuthException("NPM already registered");
        if (String.valueOf(npm).length() != 10)
            throw new AuthException("NPM is invalid");
    }
}
