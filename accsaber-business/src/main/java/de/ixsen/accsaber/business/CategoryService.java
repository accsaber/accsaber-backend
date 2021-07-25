package de.ixsen.accsaber.business;

import de.ixsen.accsaber.business.exceptions.AccsaberOperationException;
import de.ixsen.accsaber.business.exceptions.ExceptionType;
import de.ixsen.accsaber.database.model.Category;
import de.ixsen.accsaber.database.model.PlayerCategoryStats;
import de.ixsen.accsaber.database.model.players.PlayerData;
import de.ixsen.accsaber.database.repositories.model.CategoryRepository;
import de.ixsen.accsaber.database.repositories.model.PlayerDataRepository;
import de.ixsen.accsaber.database.repositories.view.CategoryAccSaberPlayerRepository;
import de.ixsen.accsaber.database.repositories.view.OverallAccSaberPlayerRepository;
import de.ixsen.accsaber.database.views.AccSaberPlayer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final PlayerDataRepository playerDataRepository;
    private final CategoryAccSaberPlayerRepository categoryAccSaberPlayerRepository;
    private final OverallAccSaberPlayerRepository overallAccSaberPlayerRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository, PlayerDataRepository playerDataRepository, OverallAccSaberPlayerRepository overallAccSaberPlayerRepository, CategoryAccSaberPlayerRepository categoryAccSaberPlayerRepository) {
        this.categoryRepository = categoryRepository;
        this.playerDataRepository = playerDataRepository;
        this.overallAccSaberPlayerRepository = overallAccSaberPlayerRepository;
        this.categoryAccSaberPlayerRepository = categoryAccSaberPlayerRepository;
    }

    public List<Category> getAllCategories() {
        return this.categoryRepository.findAll();
    }


    public void createNewCategory(Category newCategory) {
        if (this.categoryRepository.existsById(newCategory.getCategoryName())) {
            throw new AccsaberOperationException(ExceptionType.CATEGORY_ALREADY_EXISTS, String.format("Category [%s] already exists", newCategory.getCategoryName()));
        }

        newCategory = this.categoryRepository.save(newCategory);

        List<PlayerData> allPlayers = this.playerDataRepository.findAll();
        for (PlayerData player : allPlayers) {
            PlayerCategoryStats playerCategoryStats = new PlayerCategoryStats();
            playerCategoryStats.setPlayer(player);
            playerCategoryStats.setCategory(newCategory);
        }
        this.playerDataRepository.saveAll(allPlayers);

    }

    public void deleteCategory(String categoryName) {
        if (!this.categoryRepository.existsById(categoryName)) {
            throw new AccsaberOperationException(ExceptionType.CATEGORY_NOT_FOUND, String.format("Category [%s] was not found", categoryName));
        }
        this.categoryRepository.deleteById(categoryName);
    }

    public void updateDescription(String categoryName, String description) {
        Optional<Category> optionalCategory = this.categoryRepository.findById(categoryName);
        if (optionalCategory.isEmpty()) {
            throw new AccsaberOperationException(ExceptionType.CATEGORY_NOT_FOUND, String.format("Category [%s] was not found", categoryName));
        }

        Category category = optionalCategory.get();
        category.setDescription(description);
        this.categoryRepository.save(category);
    }

    public List<AccSaberPlayer> getStandingsForCategory(String categoryName) {
        if (categoryName.equals("overall")) {
            return this.overallAccSaberPlayerRepository.findAll();
        }
        return this.categoryAccSaberPlayerRepository.findAllByCategoryName(categoryName);
    }
}
