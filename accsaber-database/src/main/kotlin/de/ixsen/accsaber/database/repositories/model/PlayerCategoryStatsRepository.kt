package de.ixsen.accsaber.database.repositories.model

import de.ixsen.accsaber.database.model.PlayerCategoryStats
import de.ixsen.accsaber.database.model.players.PlayerData
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PlayerCategoryStatsRepository : JpaRepository<PlayerCategoryStats, Long> {
    fun deleteByPlayer(player: PlayerData)
}
