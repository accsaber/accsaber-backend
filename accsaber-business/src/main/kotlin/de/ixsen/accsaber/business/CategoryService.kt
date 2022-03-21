package de.ixsen.accsaber.business

import de.ixsen.accsaber.business.exceptions.AccsaberOperationException
import de.ixsen.accsaber.business.exceptions.ExceptionType
import de.ixsen.accsaber.database.model.Category
import de.ixsen.accsaber.database.model.PlayerCategoryStats
import de.ixsen.accsaber.database.repositories.model.CategoryRepository
import de.ixsen.accsaber.database.repositories.model.PlayerDataRepository
import de.ixsen.accsaber.database.repositories.view.CategoryAccSaberPlayerRepository
import de.ixsen.accsaber.database.repositories.view.OverallAccSaberPlayerRepository
import de.ixsen.accsaber.database.views.AccSaberPlayer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class CategoryService @Autowired constructor(
    private val categoryRepository: CategoryRepository,
    private val playerDataRepository: PlayerDataRepository,
    private val overallAccSaberPlayerRepository: OverallAccSaberPlayerRepository,
    private val categoryAccSaberPlayerRepository: CategoryAccSaberPlayerRepository
) {

    fun getAllCategories(): List<Category> = categoryRepository.findAll()

    fun getAllCategoriesIncludingJointCategories(): List<Category> {
            val overall = Category(
                "overall",
                "This category includes all maps from the categories counting towards the overall leaderboard.",
                "Overall Maps", true
            )

            val all = Category(
                "all", "This category includes all maps ranked on any category on AccSaber.",
                "All Ranked Maps", true
            )

            val categories = categoryRepository.findAll()
            categories.add(overall)
            categories.add(all)
            return categories
        }

    fun createNewCategory(newCategory: Category) {
        if (categoryRepository.existsByCategoryName(newCategory.categoryName)) {
            throw AccsaberOperationException(ExceptionType.CATEGORY_ALREADY_EXISTS, String.format("Category [%s] already exists", newCategory.categoryName))
        }
        val newCreatedCategory = categoryRepository.save(newCategory)
        val allPlayers = playerDataRepository.findAll()
        for (player in allPlayers) {
            val playerCategoryStats = PlayerCategoryStats(player, newCreatedCategory)
            player.playerCategoryStats.add(playerCategoryStats)
        }
        playerDataRepository.saveAll(allPlayers)
    }

    fun getStandingsForCategory(categoryName: String): List<AccSaberPlayer> {
        if (categoryName == "overall") {
            return overallAccSaberPlayerRepository.findAll()
        }
        if (!categoryRepository.existsByCategoryName(categoryName)) {
            throw AccsaberOperationException(ExceptionType.CATEGORY_NOT_FOUND, String.format("The category [%s] was not found.", categoryName))
        }
        return categoryAccSaberPlayerRepository.findAllByCategoryName(categoryName)
    }
}