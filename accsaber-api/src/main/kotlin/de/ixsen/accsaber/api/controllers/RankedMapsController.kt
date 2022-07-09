package de.ixsen.accsaber.api.controllers

import com.fasterxml.jackson.core.JsonProcessingException
import de.ixsen.accsaber.api.dtos.CreateRankedMapDto
import de.ixsen.accsaber.api.dtos.RankedMapDto
import de.ixsen.accsaber.api.dtos.RankedMapsStatisticsDto
import de.ixsen.accsaber.api.dtos.UpdateComplexityDto
import de.ixsen.accsaber.api.mapping.RankedMapMapper
import de.ixsen.accsaber.business.CategoryService
import de.ixsen.accsaber.business.PlayerService
import de.ixsen.accsaber.business.PlaylistService
import de.ixsen.accsaber.business.RankedMapService
import de.ixsen.accsaber.database.model.maps.BeatMap
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/ranked-maps")
class RankedMapsController @Autowired constructor(
    private val rankedMapService: RankedMapService,
    private val categoryService: CategoryService,
    private val playerService: PlayerService,
    private val playlistService: PlaylistService,
    private val rankedMapMapper: RankedMapMapper,
) {
    // FIXME The recognition should actually try to evaluate if the hash is a valid hash
    @PostMapping
    fun addNewRankedMapByKey(@RequestBody rankedMapDto: CreateRankedMapDto): ResponseEntity<*> {
        if (rankedMapDto.id!!.length > 10) {
            this.rankedMapService.addNewRankedMapByHash(rankedMapDto.id, rankedMapDto.difficulty!!, rankedMapDto.complexity!!, rankedMapDto.categoryName!!)
        } else {
            this.rankedMapService.addNewRankedMapByKey(rankedMapDto.id, rankedMapDto.difficulty!!, rankedMapDto.complexity!!, rankedMapDto.categoryName!!)
        }
        return ResponseEntity.noContent().build<Any>()
    }

    @PutMapping("{leaderboardId}")
    fun updateComplexity(@RequestBody updateComplexityDto: UpdateComplexityDto, @PathVariable leaderboardId: String) {
        this.rankedMapService.updateComplexity(updateComplexityDto.leaderboardId, updateComplexityDto.complexity)
    }

    @PutMapping
    fun updateComplexities(@RequestBody updateComplexityDtos: List<UpdateComplexityDto>) {
        updateComplexityDtos.forEach { this.rankedMapService.updateComplexity(it.leaderboardId, it.complexity) }
    }

    @GetMapping
    fun getAllRankedMaps(): ResponseEntity<List<RankedMapDto>> {
        val rankedMapDtos = this
            .rankedMapMapper.rankedMapsToDtos(this.rankedMapService.getAllRankedMaps())
        return ResponseEntity.ok(rankedMapDtos)
    }

    @GetMapping("/category/{categoryName}")
    fun getAllRankedMapsForCategory(@PathVariable categoryName: String): ResponseEntity<List<RankedMapDto>> {
        val category = categoryService.getCategoryByName(categoryName)
        val categoryRankedMaps = this.rankedMapService.getAllRankedMapsForCategory(category)
        val rankedMapDtos = this.rankedMapMapper.rankedMapsToDtos(categoryRankedMaps)
        return ResponseEntity.ok(rankedMapDtos)
    }

    @DeleteMapping("/{leaderboardId}")
    fun removeRankedMap(@PathVariable leaderboardId: Long): ResponseEntity<*> {
        this.rankedMapService.removeRankedMap(leaderboardId)
        playerService.recalculateApForAllPlayers()
        return ResponseEntity.noContent().build<Any>()
    }

    @GetMapping("/statistics")
    fun getRnkedStat(): ResponseEntity<RankedMapsStatisticsDto> {
        val beatMaps = this.rankedMapService.getAllRankedMaps()
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
        val beatMap = this.rankedMapService.getRankedMap(leaderboardId)
        val rankedMapDto = this.rankedMapMapper.rankedMapToDto(beatMap)
        return ResponseEntity.ok(rankedMapDto)
    }

    @Throws(JsonProcessingException::class)
    @GetMapping("/playlist")
    fun rankedMapPlaylist(): ResponseEntity<ByteArray> {
        val playlistJson = playlistService.getPlaylist("all")
        return ResponseEntity
            .ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=accsaber-rankedmaps.json")
            .body(playlistJson)
    }
}