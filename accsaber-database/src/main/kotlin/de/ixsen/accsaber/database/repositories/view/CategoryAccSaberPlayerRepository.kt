package de.ixsen.accsaber.database.repositories.view

import de.ixsen.accsaber.database.repositories.ReadOnlyRepository
import de.ixsen.accsaber.database.views.AccSaberPlayer
import de.ixsen.accsaber.database.views.CategoryAccSaberPlayer
import org.springframework.stereotype.Repository

@Repository
interface CategoryAccSaberPlayerRepository : ReadOnlyRepository<CategoryAccSaberPlayer, String, AccSaberPlayer> {
    fun findAllByCategoryName(categoryName: String): List<AccSaberPlayer>
    fun findPlayerByPlayerIdAndCategoryName(playerId: Long, category: String): AccSaberPlayer
}