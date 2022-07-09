package de.ixsen.accsaber.api.controllers

import de.ixsen.accsaber.api.dtos.MapLeaderboardPlayerDto
import de.ixsen.accsaber.api.mapping.MapLeaderboardPlayerMapper
import de.ixsen.accsaber.business.ScoreService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("map-leaderboards")
class MapLeaderboardsController(private val scoreService: ScoreService, private val mapLeaderboardPlayerMapper: MapLeaderboardPlayerMapper) {
    @GetMapping("/{mapHash}/{characteristic}/{difficulty}")
    fun getMapLeaderboards(
        @PathVariable mapHash: String,
        @PathVariable characteristic: String,
        @PathVariable difficulty: String,
        @RequestParam page: Int,
        @RequestParam pageSize: Int
    ): ResponseEntity<List<MapLeaderboardPlayerDto>> {
        val scores = scoreService.getScoresForMapHash(mapHash, characteristic, difficulty, page, pageSize)
        val mapLeaderboardPlayerDtos = this.mapLeaderboardPlayerMapper.rankedScoresToMapLeaderboardDtos(scores)
        return ResponseEntity.ok(mapLeaderboardPlayerDtos)
    }

    @GetMapping("/{mapHash}/{characteristic}/{difficulty}/around/{playerId}")
    fun getMapLeaderboardsAroundPlayer(
        @PathVariable mapHash: String,
        @PathVariable characteristic: String,
        @PathVariable difficulty: String,
        @PathVariable playerId: String
    ) : ResponseEntity<List<MapLeaderboardPlayerDto>> {
        val scores = scoreService.getScoresForMapHashAroundPlayer(mapHash, characteristic, difficulty, playerId)
        val mapLeaderboardPlayerDtos = this.mapLeaderboardPlayerMapper.rankedScoresToMapLeaderboardDtos(scores)
        return ResponseEntity.ok(mapLeaderboardPlayerDtos)
    }

    @GetMapping("/{leaderboardId}")
    fun getMapLeaderboardsByHash(@PathVariable leaderboardId: Long): ResponseEntity<List<MapLeaderboardPlayerDto>> {
        val scores = scoreService.getScoresForLeaderboardId(leaderboardId)
        val mapLeaderboardPlayerDtos = this.mapLeaderboardPlayerMapper.rankedScoresToMapLeaderboardDtos(scores)
        return ResponseEntity.ok(mapLeaderboardPlayerDtos)
    }
}