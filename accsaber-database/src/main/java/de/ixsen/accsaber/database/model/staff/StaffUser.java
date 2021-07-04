package de.ixsen.accsaber.database.model.staff;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        StaffUser staffUser = (StaffUser) o;
        return this.username.equals(staffUser.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.username);
    }
}
