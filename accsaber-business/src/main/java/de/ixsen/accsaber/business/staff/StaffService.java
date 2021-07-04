package de.ixsen.accsaber.business.staff;

import de.ixsen.accsaber.database.model.staff.StaffUser;
import de.ixsen.accsaber.database.repositories.StaffUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class StaffService {

    private final PasswordEncoder bCryptPasswordEncoder;
    private final StaffUserRepository staffUserRepository;

    @Autowired
    public StaffService(PasswordEncoder bCryptPasswordEncoder, StaffUserRepository staffUserRepository) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.staffUserRepository = staffUserRepository;
    }

    public StaffUser saveNewUser(String username, String password) {
        StaffUser staffUser = new StaffUser();
        staffUser.setUsername(username);
        staffUser.setPassword(this.bCryptPasswordEncoder.encode(password));

        this.staffUserRepository.save(staffUser);
        return staffUser;
    }

    public StaffUser findUserByName(String name) {
        return this.staffUserRepository.findByUsername(name);
    }
}
