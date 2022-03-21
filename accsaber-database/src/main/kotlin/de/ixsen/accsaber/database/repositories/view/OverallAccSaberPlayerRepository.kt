package de.ixsen.accsaber.database.repositories.view

import de.ixsen.accsaber.database.repositories.ReadOnlyRepository
import de.ixsen.accsaber.database.views.AccSaberPlayer
import de.ixsen.accsaber.database.views.OverallAccSaberPlayer
import org.springframework.stereotype.Repository

@Repository
interface OverallAccSaberPlayerRepository : ReadOnlyRepository<OverallAccSaberPlayer, Long, AccSaberPlayer> {
    fun findPlayerByPlayerId(playerId: Long): AccSaberPlayer?
}