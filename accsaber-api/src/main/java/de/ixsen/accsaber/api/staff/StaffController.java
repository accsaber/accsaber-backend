package de.ixsen.accsaber.api.staff;

import de.ixsen.accsaber.business.staff.StaffService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("staff")
public class StaffController {

    private final StaffService staffService;

    public StaffController(StaffService staffService) {
        this.staffService = staffService;
    }

    @PostMapping
    public ResponseEntity<?> createNewUser(@RequestBody StaffUserDto staffUserDto) {
        this.staffService.saveNewUser(staffUserDto.getUsername(), staffUserDto.getPassword());

        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<?> checkLogin() {
        return ResponseEntity.noContent().build();
    }
}
