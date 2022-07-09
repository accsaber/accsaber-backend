package de.ixsen.accsaber.database.repositories.view

import de.ixsen.accsaber.database.model.Category
import de.ixsen.accsaber.database.model.players.PlayerData
import de.ixsen.accsaber.database.repositories.ReadOnlyRepository
import de.ixsen.accsaber.database.views.AccSaberScore
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface AccSaberScoreRepository : ReadOnlyRepository<AccSaberScore, Long, AccSaberScore> {
    fun findAllByLeaderboardId(leaderboardId: Long): List<AccSaberScore>
    fun findAllByLeaderboardId(leaderboardId: Long, pageRequest: Pageable): List<AccSaberScore>

    fun findAllByPlayerOrderByApDesc(playerData: PlayerData): List<AccSaberScore>

    @Query(
        """
            from AccSaberScore ass
            where ass.player = ?1 and ass.beatMap.category = ?2
            order by ass.ap desc 
        """
    )
    fun findAllByPlayerAndCategoryOrderByApDesc(playerData: PlayerData, category: Category): List<AccSaberScore>

    @Query("SELECT * FROM acc_saber_score ass where ass.map_leaderboard_id = ?1 limit 10 offset ?2", nativeQuery = true)
    fun findAllByLeaderboardIdAround(leaderboardId: Long, position: Long?): List<AccSaberScore>

    @Query("FROM AccSaberScore where leaderboardId = ?1 and player.playerId = ?2")
    fun findByLeaderboardIdAndPlayer(leaderboardId: Long, playerId: Long): AccSaberScore?
}