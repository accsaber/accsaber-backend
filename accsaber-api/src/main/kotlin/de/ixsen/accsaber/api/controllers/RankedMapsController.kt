package de.ixsen.accsaber.api.controllers

import com.fasterxml.jackson.core.JsonProcessingException
import de.ixsen.accsaber.api.dtos.CreateRankedMapDto
import de.ixsen.accsaber.api.dtos.RankedMapDto
import de.ixsen.accsaber.api.dtos.RankedMapsStatisticsDto
import de.ixsen.accsaber.api.mapping.MappingComponent
import de.ixsen.accsaber.business.PlayerService
import de.ixsen.accsaber.business.PlaylistService
import de.ixsen.accsaber.business.RankedMapService
import de.ixsen.accsaber.database.model.maps.BeatMap
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.function.Function
import java.util.stream.Collectors

@RestController
@RequestMapping("/ranked-maps")
class RankedMapsController @Autowired constructor(
    private val rankedMapService: RankedMapService,
    private val playerService: PlayerService,
    private val playlistService: PlaylistService,
    private val mappingComponent: MappingComponent
) {
    // FIXME The recognition should actually try to evaluate if the hash is a valid hash
    @PostMapping
    fun addNewRankedMapByKey(@RequestBody rankedMapDto: CreateRankedMapDto): ResponseEntity<*> {
        if (rankedMapDto.id!!.length > 10) {
            rankedMapService.addNewRankedMapByHash(rankedMapDto.id, rankedMapDto.difficulty!!, rankedMapDto.complexity!!, rankedMapDto.categoryName!!)
        } else {
            rankedMapService.addNewRankedMapByKey(rankedMapDto.id, rankedMapDto.difficulty!!, rankedMapDto.complexity!!, rankedMapDto.categoryName!!)
        }
        return ResponseEntity.noContent().build<Any>()
    }

    @get:GetMapping
    val allRankedMaps: ResponseEntity<List<RankedMapDto>>
        get() {
            val rankedMapDtos = mappingComponent
                .rankedMapMapper.rankedMapsToDtos(rankedMapService.rankedMaps)
            return ResponseEntity.ok(rankedMapDtos)
        }

    @DeleteMapping("/{leaderboardId}")
    fun removeRankedMap(@PathVariable leaderboardId: Long): ResponseEntity<*> {
        rankedMapService.removeRankedMap(leaderboardId)
        playerService.recalculateApForAllPlayers()
        return ResponseEntity.noContent().build<Any>()
    }

    @GetMapping("/statistics")
    fun getRnkedStat(): ResponseEntity<RankedMapsStatisticsDto> {
        val beatMaps = rankedMapService.rankedMaps
        val trueAccCount = beatMaps.stream().filter { r: BeatMap -> r.complexity <= 4 }.count()
        val techAccCount = beatMaps.stream().filter { r: BeatMap -> r.complexity >= 10 }.count()

        val complexityToMapCount = beatMaps.groupingBy { it.complexity }.eachCount()

        val rankedMapsStatisticsDto = RankedMapsStatisticsDto(
            beatMaps.size, trueAccCount, beatMaps.size - trueAccCount - techAccCount, techAccCount, complexityToMapCount
        )

        return ResponseEntity.ok(rankedMapsStatisticsDto)
    }

    @GetMapping("/{leaderboardId}")
    fun getRankedMapDetails(@PathVariable leaderboardId: Long): ResponseEntity<RankedMapDto> {
        val beatMap = rankedMapService.getRankedMap(leaderboardId)
        val rankedMapDto = mappingComponent.rankedMapMapper.rankedMapToDto(beatMap)
        return ResponseEntity.ok(rankedMapDto)
    }

    @get:Throws(JsonProcessingException::class)
    @get:GetMapping("/playlist")
    val rankedMapPlaylist: ResponseEntity<ByteArray>
        get() {
            val playlistJson = playlistService.getPlaylist("all")
            return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=accsaber-rankedmaps.json")
                .body(playlistJson)
        }
}