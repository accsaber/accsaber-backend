package de.ixsen.accsaber.database.repositories.model;

import de.ixsen.accsaber.database.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, String> {

    boolean existsByCategoryName(String categoryName);

    Optional<Category> findByCategoryName(String categoryName);

    void deleteByCategoryName(String categoryName);
}
