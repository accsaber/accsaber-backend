package de.ixsen.accsaber.database.repositories.model

import de.ixsen.accsaber.database.model.players.PlayerData
import de.ixsen.accsaber.database.model.players.ScoreData
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.jpa.repository.query.Procedure
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ScoreDataRepository : JpaRepository<ScoreData, Long> {
    fun findAllByLeaderboardId(leaderboardId: Long): List<ScoreData>

    @Query("from ScoreData scoreData join fetch BeatMap beatMap on scoreData.beatMap = beatMap")
    fun findAllRankedScores(): List<ScoreData>
    fun findByPlayerAndLeaderboardId(player: PlayerData, leaderboardId: Long): Optional<ScoreData>

    @Procedure(procedureName = "rank_scores")
    fun rankScores(leaderboard_id: Long, maxScore: Int, complexity: Double)
}