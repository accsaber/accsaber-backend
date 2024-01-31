package de.ixsen.accsaber.business

import de.ixsen.accsaber.business.exceptions.AccsaberOperationException
import de.ixsen.accsaber.business.exceptions.ExceptionType
import de.ixsen.accsaber.business.exceptions.FetchScoreException
import de.ixsen.accsaber.business.mapping.BusinessMappingComponent
import de.ixsen.accsaber.database.model.Category
import de.ixsen.accsaber.database.model.PlayerCategoryStats
import de.ixsen.accsaber.database.model.maps.BeatMap
import de.ixsen.accsaber.database.model.players.PlayerData
import de.ixsen.accsaber.database.model.players.PlayerRankHistory
import de.ixsen.accsaber.database.model.players.ScoreData
import de.ixsen.accsaber.database.repositories.model.CategoryRepository
import de.ixsen.accsaber.database.repositories.model.PlayerDataRepository
import de.ixsen.accsaber.database.repositories.model.PlayerRankHistoryRepository
import de.ixsen.accsaber.database.repositories.model.ScoreDataRepository
import de.ixsen.accsaber.database.repositories.view.CategoryAccSaberPlayerRepository
import de.ixsen.accsaber.database.repositories.view.OverallAccSaberPlayerRepository
import de.ixsen.accsaber.database.views.AccSaberPlayer
import de.ixsen.accsaber.integration.connector.ScoreSaberConnector
import de.ixsen.accsaber.integration.model.scoresaber.player.ScoreSaberPlayerDto
import de.ixsen.accsaber.integration.model.scoresaber.score.ScoreSaberScoreBundleDto
import de.ixsen.accsaber.integration.model.scoresaber.score.ScoreSaberScoreListDto
import org.hibernate.Hibernate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.io.FileOutputStream
import java.io.IOException
import java.time.Duration
import java.time.Instant
import java.util.function.Consumer
import java.util.stream.Collectors
import javax.transaction.Transactional
import kotlin.math.ceil

@Service
class PlayerService @Autowired constructor(
    private val playerDataRepository: PlayerDataRepository,
    private val overallAccSaberPlayerRepository: OverallAccSaberPlayerRepository,
    private val categoryAccSaberPlayerRepository: CategoryAccSaberPlayerRepository,
    private val playerRankHistoryRepository: PlayerRankHistoryRepository,
    private val scoreDataRepository: ScoreDataRepository,
    private val scoreSaberConnector: ScoreSaberConnector,
    private val mappingComponent: BusinessMappingComponent,
    private val categoryRepository: CategoryRepository,
    @Value("\${accsaber.image-save-location}") imageFolder: String
) : HasLogger {
    private val avatarFolder: String = "$imageFolder/avatars"

    @Transactional
    fun getAllPlayers(): List<AccSaberPlayer> = overallAccSaberPlayerRepository.findAll()

    fun signupPlayer(playerId: Long, playerName: String) {
        if (playerDataRepository.existsById(playerId)) {
            throw AccsaberOperationException(ExceptionType.PLAYER_ALREADY_EXISTS, String.format("Player with ID %s already exists.", playerId))
        }
        val player = PlayerData(
            playerId, playerName
        )

        val playerCategoryStatsList = categoryRepository.findAll().stream()
            .map { category: Category ->
                val playerCategoryStats = PlayerCategoryStats(player, category)
                playerCategoryStats
            }
            .collect(Collectors.toSet())
        player.playerCategoryStats = playerCategoryStatsList
        playerDataRepository.save(player)
    }

    fun getRankedPlayer(playerId: Long): AccSaberPlayer? = overallAccSaberPlayerRepository.findPlayerByPlayerId(playerId)

    fun getRankedPlayerForCategory(playerId: Long, category: String): AccSaberPlayer? {
        return if (category == "overall") {
            getRankedPlayer(playerId)
        } else {
            categoryAccSaberPlayerRepository.findPlayerByPlayerIdAndCategoryName(playerId, category)
        }
    }

    fun getPlayer(playerId: Long): PlayerData {
        return playerDataRepository.findById(playerId)
            .orElseThrow { AccsaberOperationException(ExceptionType.PLAYER_NOT_FOUND, "Player with ID $playerId does not exist.") }
    }

    @Transactional
    fun loadAllPlayerIds(): List<Long> {
        return playerDataRepository.findAllPlayerIds()
    }

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    fun handlePlayer(allRankedMaps: List<BeatMap>, playerId: Long?) {
        val optPlayer = playerDataRepository.findById(playerId!!)
        if (optPlayer.isEmpty) {
            this.getLogger().warn("Previously loaded player with id [{}] was not found in the database", playerId)
            return
        }

        val player = optPlayer.get()
        Hibernate.initialize(player.scores)
        var playerData = getScoreSaberPlayerData(player.playerId)
        if (playerData == null) {
            this.getLogger().error("Couldn't fetch player with id {}, skipping", player.playerId)
            return
        }

        if (player.avatarUrl == null) {
            loadAvatar(player.playerId, playerData.profilePicture!!)
        }

        this.getLogger().info("Loading {} scores for {}.", playerData.scoreStats!!.totalPlayCount, playerData.name)
        mappingComponent.playerMapper.scoreSaberPlayerToPlayer(player, playerData)
        val totalPlayCount = playerData.scoreStats!!.totalPlayCount
        val pageCount = ceil(totalPlayCount / 100.0).toInt()
        val scoreIterator: Iterator<ScoreData> = ArrayList(player.scores).iterator()

        var score: ScoreData? = null
        if (scoreIterator.hasNext()) {
            score = scoreIterator.next()
        }

        val newlySetScores: MutableList<ScoreData> = ArrayList()
        var page = 1
        while (page <= pageCount) {
            this.getLogger().trace("Loading page {} for {}.", page, playerData.name)
            val scoreSaberScores = getScoreSaberScore(player.playerId, page)
            for (scoreSaberScoreBundleDto in scoreSaberScores.playerScores) {
                val areScoreIdsIdentical = score != null && score.scoreId == scoreSaberScoreBundleDto.score?.id!!
                if (areScoreIdsIdentical) {
                    if (score!!.timeSet == Instant.parse(scoreSaberScoreBundleDto.score?.timeSet!!)) {
                        page = pageCount + 1
                        break
                    } else if (scoreIterator.hasNext()) {
                        score = scoreIterator.next()
                    } else {
                        score = null
                    }
                }
                this.handleSetScores(player, newlySetScores, scoreSaberScoreBundleDto)
            }
            page++
        }
        playerData = getScoreSaberPlayerData(player.playerId)
        if (playerData != null) {
            val newMaxPage = ceil(playerData.scoreStats!!.totalPlayCount / 100.0).toInt()
            if (pageCount < newMaxPage && score == null) {
                this.getLogger().trace("Player {} has set a score that created a new page, while scores were loading, reloading latest page.", player.playerName)
                val scoreSaberScores = getScoreSaberScore(player.playerId, newMaxPage)
                for (scoreSaberScoreBundleDto in scoreSaberScores.playerScores) {
                    this.handleSetScores(player, newlySetScores, scoreSaberScoreBundleDto)
                }
            }
        }

        scoreDataRepository.saveAll(newlySetScores)
        playerDataRepository.save(player)
        scoreDataRepository.flush()
        playerDataRepository.flush()

        val categoryIds = newlySetScores.stream()
            .filter { tuple: ScoreData? -> allRankedMaps.stream().anyMatch { map: BeatMap -> map.leaderboardId == tuple!!.leaderboardId } }
            .map { scoreTuple: ScoreData? ->
                allRankedMaps.stream().filter { map: BeatMap -> map.leaderboardId == scoreTuple!!.leaderboardId }
                    .findFirst().get().category.id
            }
            .distinct()
            .collect(Collectors.toList())
        if (newlySetScores.isEmpty()) {
            return
        }
        this.getLogger().trace(String.format("Recalculating player AP for %s after successfully loading new scores", player.playerName))
        playerDataRepository.recalcPlayerAp(player.playerId)
        if (categoryIds.size == 3) {
            playerDataRepository.recalculatePlayerCategoryStats(player.playerId)
        } else {
            for (categoryId in categoryIds) {
                playerDataRepository.recalculatePlayerCategoryStat(player.playerId, categoryId)
            }
        }
    }

    private fun handleSetScores(player: PlayerData, newlySetScores: MutableList<ScoreData>, scoreSaberScoreBundleDto: ScoreSaberScoreBundleDto) {
        val optScore = player.scores.stream().filter { score: ScoreData -> score.scoreId == scoreSaberScoreBundleDto.score?.id!! }.findFirst()
        val score: ScoreData
        if (optScore.isPresent) {
            score = mappingComponent.scoreMapper.scoreSaberScoreDtoToExistingScore(optScore.get(), scoreSaberScoreBundleDto)
        } else {
            score = mappingComponent.scoreMapper.scoreSaberScoreDtoToScore(scoreSaberScoreBundleDto)
            player.addScore(score)
        }
        newlySetScores.add(score)
    }

    @Transactional
    fun recalculateApForAllPlayers() {
        playerDataRepository.recalculateAllAp()
    }

    fun loadAvatars() {
        val start = Instant.now()
        this.getLogger().info("Loading avatars..")
        playerDataRepository.findAll().forEach(Consumer { player: PlayerData ->
            if (player.avatarUrl != null) {
                loadAvatar(player.playerId, player.avatarUrl!!)
            }
        })
        this.getLogger().info("Loading avatars finished in {} seconds.", Duration.between(start, Instant.now()).seconds)
    }

    fun takeRankingSnapshot() {
        this.getLogger().trace("Taking snapshot of current ranks")
        this.playerDataRepository.takeRankSnapshot()
    }

    fun getRecentPlayerRankHistory(playerId: Long): List<PlayerRankHistory> {
        return playerRankHistoryRepository.findLastMonthForPlayer(playerId)
    }

    fun getRecentPlayerRankHistoryForCategory(playerId: Long, categoryName: String): List<PlayerRankHistory> {
        val category = categoryRepository.findByCategoryName(categoryName)
            .orElseThrow { AccsaberOperationException(ExceptionType.CATEGORY_NOT_FOUND, String.format("Category [%s] does not exist.", categoryName)) }
        return playerRankHistoryRepository.findLastMonthForPlayerAndCategory(playerId, category.id)
    }

    fun refreshMaterializedViews() {
        this.playerDataRepository.refreshMaterializedViews()
    }

    /**
     * Loads the avatar of a player.
     */
    private fun loadAvatar(playerId: Long, avatarUrl: String) {
        val avatar = scoreSaberConnector.loadAvatar(avatarUrl)
        try {
            FileOutputStream("$avatarFolder/$playerId.jpg").use { fileOutputStream -> fileOutputStream.write(avatar) }
        } catch (e: IOException) {
            this.getLogger().error("Unable to save avatar for player with playerId {}", playerId, e)
        }
    }

    private fun getScoreSaberPlayerData(playerId: Long): ScoreSaberPlayerDto? {
        return tryGetScoreSaberPlayerData(playerId, 0)
    }

    // TODO recheck whether recursion is the way to go here.
    private fun tryGetScoreSaberPlayerData(playerId: Long, tryCount: Int): ScoreSaberPlayerDto? {
        return try {
            scoreSaberConnector.getPlayerData(playerId)
        } catch (e: Exception) {
            if (tryCount < 4) {
                this.getLogger().warn("Fetching failed for player with id $playerId. Retrying", e)
                return tryGetScoreSaberPlayerData(playerId, tryCount + 1)
            } else {
                this.getLogger().error("Fetching failed after multiple tries. Scores will not update.", e)
                return null
            }
        }
    }

    private fun getScoreSaberScore(playerId: Long, page: Int): ScoreSaberScoreListDto {
        return tryGetScoreSaberScore(playerId, page, 0)
    }

    // TODO recheck whether recursion is the way to go here.
    private fun tryGetScoreSaberScore(playerId: Long, page: Int, tryCount: Int): ScoreSaberScoreListDto {
        return try {
            scoreSaberConnector.getPlayerScores(playerId, page)!!
        } catch (e: Exception) {
            if (tryCount < 4) {
                this.getLogger().warn("Fetching scores failed for player with id $playerId. Retrying", e)
                tryGetScoreSaberScore(playerId, page, tryCount + 1)
            } else {
                throw FetchScoreException("Fetching scores failed after multiple tries. Scores will not update.", e)
            }
        }
    }
}
