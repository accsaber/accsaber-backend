package de.ixsen.accsaber.api.controllers

import de.ixsen.accsaber.api.dtos.MapLeaderboardPlayerDto
import de.ixsen.accsaber.api.mapping.MappingComponent
import de.ixsen.accsaber.business.ScoreService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("map-leaderboards")
class MapLeaderboardsController(private val scoreService: ScoreService, private val mappingComponent: MappingComponent) {
    @GetMapping("/{mapHash}/{characteristic}/{difficulty}")
    
    fun getMapLeaderboards(
        @PathVariable mapHash: String,
        @PathVariable characteristic: String,
        @PathVariable difficulty: String,
        @RequestParam page: Int,
        @RequestParam pageSize: Int
    ): ResponseEntity<List<MapLeaderboardPlayerDto>> {
        val scores = scoreService.getScoresForMapHash(mapHash, characteristic, difficulty, page, pageSize)
        val mapLeaderboardPlayerDtos = mappingComponent.mapLeaderboardPlayerMapper.rankedScoresToMapLeaderboardDtos(scores)
        return ResponseEntity.ok(mapLeaderboardPlayerDtos)
    }

    @GetMapping("/{leaderboardId}")
    fun getMapLeaderboardsByHash(@PathVariable leaderboardId: Long): ResponseEntity<List<MapLeaderboardPlayerDto>> {
        val scores = scoreService.getScoresForLeaderboardId(leaderboardId)
        val mapLeaderboardPlayerDtos = mappingComponent.mapLeaderboardPlayerMapper.rankedScoresToMapLeaderboardDtos(scores)
        return ResponseEntity.ok(mapLeaderboardPlayerDtos)
    }
}