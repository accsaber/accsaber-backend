package de.ixsen.accsaber.database.repositories.model

import de.ixsen.accsaber.database.model.players.PlayerData
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.jpa.repository.query.Procedure
import org.springframework.stereotype.Repository

@Repository
interface PlayerDataRepository : JpaRepository<PlayerData, Long> {
    @Query("select player.playerId from PlayerData player order by player.joinDate desc")
    fun findAllPlayerIds(): List<Long>

    @Procedure(procedureName = "recalc_all_ap")
    fun recalculateAllAp()

    @Procedure(procedureName = "recalc_player_category_stat")
    fun recalculatePlayerCategoryStat(playerId: Long, categoryId: Long)

    @Procedure(procedureName = "recalc_player_category_stats")
    fun recalculatePlayerCategoryStats(playerId: Long)

    @Procedure(procedureName = "recalc_player_ap")
    fun recalcPlayerAp(playerId: Long)

    @Procedure(procedureName = "take_ranking_snapshot")
    fun takeRankSnapshot()
}