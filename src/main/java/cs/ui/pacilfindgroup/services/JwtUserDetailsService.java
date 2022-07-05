package cs.ui.pacilfindgroup.services;

import cs.ui.pacilfindgroup.exceptions.ApiRequestException;
import cs.ui.pacilfindgroup.exceptions.UsernameNotFoundException;
import cs.ui.pacilfindgroup.repository.PacilUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

@Service("jwtuserdetailsservice")
public class JwtUserDetailsService implements UserDetailsService {
    @Autowired
    private PacilUserRepository pacilUserRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws ApiRequestException {
        var user = pacilUserRepository.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }

        Collection<GrantedAuthority> authorities = new ArrayList<>();
        if (user.isSuperuser())
            authorities.add(() -> "ROLE_SUPERUSER");

        return new User(user.getUsername(), user.getPassword(), authorities);
    }

    public boolean isSuperuser(UserDetails userDetails) {
        return userDetails.getAuthorities().contains((GrantedAuthority) () -> "ROLE_SUPERUSER");
    }

}
