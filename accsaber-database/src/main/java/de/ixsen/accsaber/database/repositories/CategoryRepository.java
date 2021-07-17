package de.ixsen.accsaber.database.repositories;

import de.ixsen.accsaber.database.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, String> {
}
