package de.ixsen.accsaber.database.repositories.model;

import de.ixsen.accsaber.database.model.staff.StaffUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StaffUserRepository extends JpaRepository<StaffUser, String> {
}
