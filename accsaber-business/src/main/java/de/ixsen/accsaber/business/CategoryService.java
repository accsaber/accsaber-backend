package de.ixsen.accsaber.business;

import de.ixsen.accsaber.business.exceptions.AccsaberOperationException;
import de.ixsen.accsaber.business.exceptions.ExceptionType;
import de.ixsen.accsaber.database.model.Category;
import de.ixsen.accsaber.database.repositories.model.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> getAllLeaderboards() {
        return this.categoryRepository.findAll();
    }


    public void createNewLeaderboard(Category newCategory) {
        if (this.categoryRepository.existsById(newCategory.getCategoryName())) {
            throw new AccsaberOperationException(ExceptionType.LEADERBOARD_ALREADY_EXISTS, String.format("Leaderboard with the id {%s} already exists", newCategory.getDescription()));
        }

        this.categoryRepository.save(newCategory);
    }

    public void deleteLeaderboard(String categoryName) {
        if (!this.categoryRepository.existsById(categoryName)) {
            throw new AccsaberOperationException(ExceptionType.LEADERBOARD_NOT_FOUND, String.format("Leaderboard with the id {%s} was not found", categoryName));
        }
        this.categoryRepository.deleteById(categoryName);
    }

    public void updateDescription(String categoryName, String description) {
        Optional<Category> optionalLeaderboard = this.categoryRepository.findById(categoryName);
        if (optionalLeaderboard.isEmpty()) {
            throw new AccsaberOperationException(ExceptionType.LEADERBOARD_NOT_FOUND, String.format("Leaderboard with the id {%s} was not found", categoryName));
        }

        Category category = optionalLeaderboard.get();
        category.setDescription(description);
        this.categoryRepository.save(category);
    }
}
