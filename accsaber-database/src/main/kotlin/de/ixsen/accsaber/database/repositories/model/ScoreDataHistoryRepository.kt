package de.ixsen.accsaber.database.repositories.model

import de.ixsen.accsaber.database.model.players.PlayerData
import de.ixsen.accsaber.database.model.players.ScoreDataHistory
import de.ixsen.accsaber.database.repositories.ReadOnlyRepository
import org.springframework.stereotype.Repository

@Repository
interface ScoreDataHistoryRepository : ReadOnlyRepository<ScoreDataHistory, Long, ScoreDataHistory> {
    fun findAllByPlayerAndLeaderboardId(playerData: PlayerData, scoreId: Long): List<ScoreDataHistory>
}