package de.ixsen.accsaber.api.controllers;

import de.ixsen.accsaber.api.dtos.MapLeaderboardPlayerDto;
import de.ixsen.accsaber.api.mapping.MappingComponent;
import de.ixsen.accsaber.business.ScoreService;
import de.ixsen.accsaber.database.views.AccSaberScore;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("map-leaderboards")
public class MapLeaderboardsController {

    private final MappingComponent mappingComponent;
    private final ScoreService scoreService;

    public MapLeaderboardsController(ScoreService scoreService, MappingComponent mappingComponent) {
        this.scoreService = scoreService;
        this.mappingComponent = mappingComponent;
    }

    @GetMapping("/{mapHash}/{characteristic}/{difficulty}")
    public ResponseEntity<List<MapLeaderboardPlayerDto>> getMapLeaderboards(@PathVariable String mapHash, @PathVariable String characteristic, @PathVariable String difficulty, @RequestParam int page, @RequestParam int pageSize) {
        List<AccSaberScore> scores = this.scoreService.getScoresForMapHash(mapHash, characteristic, difficulty, page, pageSize);
        List<MapLeaderboardPlayerDto> mapLeaderboardPlayerDtos = this.mappingComponent.getMapLeaderboardPlayerMapper().rankedScoresToMapLeaderboardDtos(scores);

        return ResponseEntity.ok(mapLeaderboardPlayerDtos);
    }

    @GetMapping("/{leaderboardId}")
    public ResponseEntity<List<MapLeaderboardPlayerDto>> getMapLeaderboardsByHash(@PathVariable Long leaderboardId) {
        List<AccSaberScore> scores = this.scoreService.getScoresForLeaderboardId(leaderboardId);
        List<MapLeaderboardPlayerDto> mapLeaderboardPlayerDtos = this.mappingComponent.getMapLeaderboardPlayerMapper().rankedScoresToMapLeaderboardDtos(scores);

        return ResponseEntity.ok(mapLeaderboardPlayerDtos);
    }
}
