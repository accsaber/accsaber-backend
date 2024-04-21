package de.ixsen.accsaber.business

import de.ixsen.accsaber.business.exceptions.AccsaberOperationException
import de.ixsen.accsaber.business.exceptions.ExceptionType
import de.ixsen.accsaber.database.model.players.PlayerData
import de.ixsen.accsaber.database.model.players.ScoreData
import de.ixsen.accsaber.database.model.players.ScoreDataHistory
import de.ixsen.accsaber.database.repositories.model.BeatMapRepository
import de.ixsen.accsaber.database.repositories.model.ScoreDataHistoryRepository
import de.ixsen.accsaber.database.repositories.model.ScoreDataRepository
import de.ixsen.accsaber.database.repositories.view.AccSaberScoreRepository
import de.ixsen.accsaber.database.views.AccSaberScore
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.lang.Long.max
import java.util.*

@Service
class ScoreService @Autowired constructor(
    private val accSaberScoreRepository: AccSaberScoreRepository,
    private val scoreDataHistoryRepository: ScoreDataHistoryRepository,
    private val scoreDataRepository: ScoreDataRepository,
    private val beatMapRepository: BeatMapRepository
) : HasLogger {

    fun getScoresForLeaderboardId(leaderboardId: Long): List<AccSaberScore> {
        return accSaberScoreRepository.findAllByLeaderboardId(leaderboardId)
    }

    @Transactional(readOnly = true)
    fun getScoresForMapHash(mapHash: String, characteristic: String, difficulty: String, page: Int, pageSize: Int): List<AccSaberScore> {
        val beatMap = beatMapRepository.findBySongAndDifficulty(mapHash, difficulty)
            .orElseThrow { AccsaberOperationException(ExceptionType.RANKED_MAP_NOT_FOUND, "The map was not found.") }
        return accSaberScoreRepository.findAllByLeaderboardId(beatMap.leaderboardId, PageRequest.of(page, pageSize))
    }

    @Transactional(readOnly = true)
    fun getScoresForMapHashAroundPlayer(mapHash: String, characteristic: String, difficulty: String, playerId: String): List<AccSaberScore> {
        val beatMap = beatMapRepository.findBySongAndDifficulty(mapHash, difficulty)
            .orElseThrow { AccsaberOperationException(ExceptionType.RANKED_MAP_NOT_FOUND, "The map was not found.") }

        val playerScore = this.accSaberScoreRepository.findByLeaderboardIdAndPlayer(beatMap.leaderboardId, playerId.toLong()) ?: return listOf()

        return accSaberScoreRepository.findAllByLeaderboardIdAround(beatMap.leaderboardId, max(playerScore.ranking!! - 6, 0L))
    }

    fun getScoresForPlayer(player: PlayerData, page: Int, pageSize: Int,): List<AccSaberScore> {
        return accSaberScoreRepository.findAllByPlayerOrderByApDesc(player, PageRequest.of(page, pageSize))
    }

    fun getRecentScoresForPlayer(player: PlayerData, page: Int, pageSize: Int,): List<AccSaberScore> {
        return accSaberScoreRepository.findAllByPlayerOrderByTimeSetDesc(player, PageRequest.of(page, pageSize))
    }

    fun getScoresForPlayer(player: PlayerData, categoryName: String): List<AccSaberScore> {
        return accSaberScoreRepository.findAllByPlayerAndCategoryOrderByApDesc(player, categoryName)
    }

    fun getScoreHistoryForPlayer(player: PlayerData, leaderboardId: Long): List<ScoreDataHistory> {
        return scoreDataHistoryRepository.findAllByPlayerAndLeaderboardId(player, leaderboardId)
    }

    fun getScoreByPlayerForLeaderboard(player: PlayerData, leaderboardId: Long): Optional<ScoreData> {
        return scoreDataRepository.findByPlayerAndLeaderboardId(player, leaderboardId)
    }
}
