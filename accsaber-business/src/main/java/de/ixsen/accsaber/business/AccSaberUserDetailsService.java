package de.ixsen.accsaber.business;

import de.ixsen.accsaber.database.model.staff.StaffUser;
import de.ixsen.accsaber.database.repositories.StaffUserRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
public class AccSaberUserDetailsService implements UserDetailsService {

    private final StaffUserRepository staffUserRepository;

    public AccSaberUserDetailsService(StaffUserRepository staffUserRepository) {
        this.staffUserRepository = staffUserRepository;
    }

    // FIXME Add the role to the granted authorities
    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Optional<StaffUser> user = this.staffUserRepository.findById(s);
        return user
                .map(staffUser -> new User(staffUser.getUsername(), staffUser.getPassword(), Collections.emptyList()))
                .orElse(null);

    }
}
