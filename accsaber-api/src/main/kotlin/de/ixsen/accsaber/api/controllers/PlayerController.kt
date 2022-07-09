package de.ixsen.accsaber.api.controllers

import de.ixsen.accsaber.api.dtos.PlayerDto
import de.ixsen.accsaber.api.dtos.PlayerScoreDto
import de.ixsen.accsaber.api.dtos.SignupDto
import de.ixsen.accsaber.api.mapping.PlayerMapper
import de.ixsen.accsaber.api.mapping.ScoreMapper
import de.ixsen.accsaber.business.CategoryService
import de.ixsen.accsaber.business.PlayerService
import de.ixsen.accsaber.business.RankedMapService
import de.ixsen.accsaber.business.ScoreService
import de.ixsen.accsaber.business.exceptions.AccsaberOperationException
import de.ixsen.accsaber.business.exceptions.ExceptionType
import de.ixsen.accsaber.database.model.players.ScoreData
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.Instant
import java.time.LocalDate

@RestController
@RequestMapping("players")
class PlayerController @Autowired constructor(
    private val playerService: PlayerService,
    private val categoryService: CategoryService,
    private val playerMapper: PlayerMapper,
    private val scoreMapper: ScoreMapper,
    private val scoreService: ScoreService,
    private val rankedMapService: RankedMapService
) {
    @PostMapping
    fun addNewPlayer(@RequestBody signupDto: SignupDto): ResponseEntity<*> {
        val playerId = Regex("(scoresaber\\.com/u/)?(\\d{16,22})[^0-9]?", RegexOption.IGNORE_CASE)
            .find(signupDto.scoresaberLink)
            ?.groupValues?.get(2) ?: throw AccsaberOperationException(
            ExceptionType.PLAYER_NOT_FOUND,
            "Player with the signupUrl ${signupDto.scoresaberLink} was not found."
        )

        playerService.signupPlayer(playerId.toLong(), signupDto.playerName)
        return ResponseEntity.noContent().build<Any>()
    }

    @GetMapping(path = ["/{playerId}"])
    fun getPlayerInfo(@PathVariable playerId: Long): ResponseEntity<PlayerDto> {
        val accSaberPlayer = playerService.getRankedPlayer(playerId)
        val playerDto = if (accSaberPlayer == null) {
            val player = playerService.getPlayer(playerId)
            this.playerMapper.rawPlayerToDto(player)
        } else {
            this.playerMapper.playerToPlayerDto(accSaberPlayer)
        }
        return ResponseEntity.ok(playerDto)
    }

    @GetMapping(path = ["/{playerId}/{category}"])
    fun getPlayerInfoForCategory(@PathVariable playerId: Long, @PathVariable category: String): ResponseEntity<PlayerDto> {
        val accSaberPlayer = playerService.getRankedPlayerForCategory(playerId, category)
        val playerDto = if (accSaberPlayer == null) {
            val player = playerService.getPlayer(playerId)
            this.playerMapper.rawPlayerToDto(player)
        } else {
            this.playerMapper.playerToPlayerDto(accSaberPlayer)
        }
        return ResponseEntity.ok(playerDto)
    }

    @GetMapping(path = ["/{playerId}/scores"])
    fun getPlayerScores(@PathVariable playerId: Long): ResponseEntity<ArrayList<PlayerScoreDto>> {
        val player = playerService.getPlayer(playerId)
        val scoresForPlayer = scoreService.getScoresForPlayer(player)
        val playerScoreDtos = this.scoreMapper.rankedScoresToPlayerScores(scoresForPlayer)
        return ResponseEntity.ok(playerScoreDtos)
    }

    @GetMapping(path = ["/{playerId}/{categoryName}/scores"])
    fun getPlayerCategoryScores(@PathVariable playerId: Long, @PathVariable categoryName: String): ResponseEntity<ArrayList<PlayerScoreDto>> {
        val player = playerService.getPlayer(playerId)
        val category = this.categoryService.getCategoryByName(categoryName)
        val scoresForPlayer = scoreService.getScoresForPlayer(player, category)
        val playerScoreDtos = this.scoreMapper.rankedScoresToPlayerScores(scoresForPlayer)
        return ResponseEntity.ok(playerScoreDtos)
    }

    @GetMapping(path = ["/{playerId}/score-history/{leaderboardId}"])
    fun getPlayerScoreHistoryForMap(@PathVariable playerId: Long, @PathVariable leaderboardId: Long): ResponseEntity<Map<Instant, Double>> {
        val player = playerService.getPlayer(playerId)
        val maxScore = rankedMapService.getRankedMap(leaderboardId).maxScore
        val scoreHistoryForPlayer = scoreService.getScoreHistoryForPlayer(player, leaderboardId)
        val currentScore = scoreService.getScoreByPlayerForLeaderboard(player, leaderboardId)

        // TODO fix this performance garbage
        val map = mutableMapOf<Instant, Double>()
        scoreHistoryForPlayer.associateTo(map) { it.timeSet to it.unmodififiedScore.toDouble() / maxScore.toDouble() }
        currentScore.ifPresent { scoreData: ScoreData -> map[scoreData.timeSet] = scoreData.unmodififiedScore / maxScore.toDouble() }
        return ResponseEntity.ok(map)
    }

    @GetMapping
    fun getPlayers(): ResponseEntity<ArrayList<PlayerDto>> {
        val accSaberPlayerEntities = playerService.getAllPlayers()
        val playerDtos = this.playerMapper.playersToPlayerDtos(accSaberPlayerEntities)
        return ResponseEntity.ok(playerDtos)
    }

    // FIXME Caching
    @GetMapping(path = ["/{playerId}/recent-rank-history"])
    fun getRecentPlayerRankHistory(@PathVariable playerId: Long): ResponseEntity<Map<LocalDate, Int>> {
        val recentPlayerRankHistory = playerService.getRecentPlayerRankHistory(playerId)
        val map = recentPlayerRankHistory.associate { it.date to it.ranking }
        return ResponseEntity.ok(map)
    }

    @GetMapping(path = ["/{playerId}/{category}/recent-rank-history"])
    fun getRecentPlayerRankHistoryForCategory(@PathVariable playerId: Long, @PathVariable category: String): ResponseEntity<Map<LocalDate, Int>> {
        val recentPlayerRankHistory = playerService.getRecentPlayerRankHistoryForCategory(playerId, category)
        val map = recentPlayerRankHistory.associate { it.date to it.ranking }
        return ResponseEntity.ok(map)
    }

    @PutMapping("/recalc-ap")
    fun recalculateApForAllPlayers(){
        this.playerService.recalculateApForAllPlayers();
    }
}