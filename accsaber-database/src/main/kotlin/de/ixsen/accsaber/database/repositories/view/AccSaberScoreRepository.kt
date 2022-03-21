package de.ixsen.accsaber.database.repositories.view

import de.ixsen.accsaber.database.model.players.PlayerData
import de.ixsen.accsaber.database.repositories.ReadOnlyRepository
import de.ixsen.accsaber.database.views.AccSaberScore
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository

@Repository
interface AccSaberScoreRepository : ReadOnlyRepository<AccSaberScore, Long, AccSaberScore> {
    fun findAllByLeaderboardId(leaderboardId: Long): List<AccSaberScore>
    fun findAllByLeaderboardId(leaderboardId: Long, pageRequest: Pageable): List<AccSaberScore>
    fun findAllByPlayerOrderByApDesc(playerData: PlayerData): List<AccSaberScore>
}