package de.ixsen.accsaber.database.model.staff;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class StaffUser {

    @Id
    private String username;
    private String password;
    private String role;

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return this.role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
