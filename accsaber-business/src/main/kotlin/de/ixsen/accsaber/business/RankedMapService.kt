package de.ixsen.accsaber.business

import de.ixsen.accsaber.business.exceptions.AccsaberOperationException
import de.ixsen.accsaber.business.exceptions.ExceptionType
import de.ixsen.accsaber.database.model.Category
import de.ixsen.accsaber.database.model.maps.BeatMap
import de.ixsen.accsaber.database.model.players.ScoreData
import de.ixsen.accsaber.database.repositories.model.BeatMapRepository
import de.ixsen.accsaber.database.repositories.model.CategoryRepository
import de.ixsen.accsaber.database.repositories.model.ScoreDataRepository
import de.ixsen.accsaber.integration.connector.BeatSaverConnector
import de.ixsen.accsaber.integration.model.beatsaver.BeatSaverMapDifficulty
import de.ixsen.accsaber.integration.model.beatsaver.BeatSaverSongInfo
import de.ixsen.accsaber.integration.model.beatsaver.BeatSaverVersion
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.function.Consumer

@Service
class RankedMapService @Autowired constructor(
    private val beatMapRepository: BeatMapRepository,
    private val scoreDataRepository: ScoreDataRepository,
    private val categoryRepository: CategoryRepository,
    private val beatSaverConnector: BeatSaverConnector,
    private val songService: SongService
) {
    fun getAllRankedMaps(): List<BeatMap> {
        return beatMapRepository.findAll()
    }

    fun getAllRankedMapsForCategory(category: Category): List<BeatMap> {
        return this.beatMapRepository.findAllForCategory(category)
    }

    fun getRankedMap(leaderboardId: Long): BeatMap {
        val optionalRankedMap = beatMapRepository.findById(leaderboardId)
        if (optionalRankedMap.isEmpty) {
            throw AccsaberOperationException(ExceptionType.RANKED_MAP_NOT_FOUND, "The ranked map with the id could not be found.")
        }
        return optionalRankedMap.get()
    }

    fun addNewRankedMapByKey(mapKey: String, difficulty: String, complexity: Double, categoryName: String) {
        val beatSaverSongInfo = beatSaverConnector.getMapInfoByKey(mapKey)!!
        val songHash = beatSaverSongInfo.versions.stream().findFirst().orElseThrow().hash
        addNewRankedMap(beatSaverSongInfo, songHash!!, difficulty, complexity, categoryName)
    }

    fun addNewRankedMapByHash(songHash: String, difficulty: String, complexity: Double, categoryName: String) {
        val beatSaverSongInfo = beatSaverConnector.getMapInfoByHash(songHash)!!
        addNewRankedMap(beatSaverSongInfo, songHash, difficulty, complexity, categoryName)
    }

    private fun addNewRankedMap(beatSaverSongInfo: BeatSaverSongInfo, songHash: String, difficulty: String, complexity: Double, categoryName: String) {
        val optionalCategory = categoryRepository.findByCategoryName(categoryName)
        if (optionalCategory.isEmpty) {
            throw AccsaberOperationException(ExceptionType.CATEGORY_NOT_FOUND, String.format("The category [%s] was not found.", categoryName))
        }
        val category = optionalCategory.get()
        val leaderBoardId = beatSaverConnector.getScoreSaberId(songHash, mapDiffToId(difficulty))
        if (beatMapRepository.existsById(leaderBoardId)) {
            throw AccsaberOperationException(ExceptionType.RANKED_MAP_ALREADY_EXISTS, "The ranked map with the leaderboardId $leaderBoardId already exists")
        }
        val beatSaverVersion = beatSaverSongInfo.versions.stream()
            .filter { version: BeatSaverVersion -> songHash.equals(version.hash, ignoreCase = true) }
            .findAny()
            .orElseThrow {
                AccsaberOperationException(
                    ExceptionType.RANKED_MAP_NOT_FOUND,
                    String.format("BeatSaver did not return requested map with hash %s", songHash)
                )
            }
        var song = songService.getOrCreateSong(beatSaverSongInfo, beatSaverVersion)
        val beatSaverMapDifficulty = beatSaverVersion.diffs.stream()
            .filter { diff: BeatSaverMapDifficulty -> difficulty.equals(diff.difficulty, ignoreCase = true) && "Standard".equals(diff.characteristic, ignoreCase = true) }
            .findAny().orElseThrow {
                AccsaberOperationException(
                    ExceptionType.RANKED_MAP_NOT_FOUND,
                    String.format("Map with hash %s does not have [%s] difficulty as Standard characteristic", songHash, difficulty)
                )
            }
        val beatMap = BeatMap(
            leaderBoardId,
            calculateMaxScore(beatSaverMapDifficulty.notes!!),
            song,
            difficulty,
            complexity,
            category
        )

        song.beatMaps.add(beatMap)
        song = songService.saveSong(song)
        scoreDataRepository.rankScores(leaderBoardId, beatMap.maxScore, complexity)
    }

    private fun mapDiffToId(difficulty: String): Int {
        when (difficulty.lowercase()) {
            "easy" -> return 1
            "normal" -> return 3
            "hard" -> return 5
            "expert" -> return 7
            "expertplus" -> return 9
        }
        return -1
    }

    fun removeRankedMap(leaderboardId: Long) {
        val map = beatMapRepository.findById(leaderboardId)
        if (map.isEmpty) {
            throw AccsaberOperationException(
                ExceptionType.RANKED_MAP_NOT_FOUND,
                "The ranked map [$leaderboardId] does not currently exist."
            )
        }
        val beatMap = map.get()
        val song = beatMap.song
        song.beatMaps.remove(beatMap)
        if (song.beatMaps.size == 0) {
            songService.removeSong(song.songHash)
        } else {
            songService.saveSong(song)
        }
        val unrankedScores = scoreDataRepository.findAllByLeaderboardId(leaderboardId)
        unrankedScores.forEach(Consumer { score: ScoreData -> score.isRankedScore = false })
        scoreDataRepository.saveAll(unrankedScores)
    }

    // There has to be a better way to do this
    private fun calculateMaxScore(noteCount: Int): Int {
        var score = 0L
        for (i in 1..noteCount) {
            score += if (i <= 1) {
                1
            } else if (i <= 5) {
                2
            } else if (i <= 13) {
                4
            } else {
                break
            }
        }
        score *= 115
        if (noteCount > 13) {
            score += 8 * (noteCount - 13) * 115
        }
        return score.toInt()
    }

    fun updateComplexity(leaderboardId: String, complexity: Double) {
        val beatMap = this.beatMapRepository.findById(leaderboardId.toLong())
            .orElseThrow { AccsaberOperationException(ExceptionType.RANKED_MAP_NOT_FOUND, "The ranked map [$leaderboardId] does not currently exist.") }

        beatMap.complexity = complexity
        this.beatMapRepository.save(beatMap)
    }
}