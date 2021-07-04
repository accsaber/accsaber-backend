package de.ixsen.accsaber.api.map_leaderboards;

import de.ixsen.accsaber.business.maps.RankedMapService;
import de.ixsen.accsaber.business.ScoreService;
import de.ixsen.accsaber.database.model.players.RankedScore;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("map-leaderboards")
public class MapLeaderboardsController {

    private final RankedMapService rankedMapService;
    private final MapLeaderboardPlayerMapper mappingComponent;
    private final ScoreService scoreService;

    public MapLeaderboardsController(RankedMapService rankedMapService, ScoreService scoreService, MapLeaderboardPlayerMapper mapLeaderboardPlayerMapper) {
        this.rankedMapService = rankedMapService;
        this.scoreService = scoreService;
        this.mappingComponent = mapLeaderboardPlayerMapper;
    }

    @GetMapping("/{leaderboardId}")
    public ResponseEntity<List<MapLeaderboardPlayerDto>> getMapLeaderboards(@PathVariable Long leaderboardId) {
        List<RankedScore> scores = this.scoreService.getScoresForLeaderboardId(leaderboardId);
        List<MapLeaderboardPlayerDto> mapLeaderboardPlayerDtos = this.mappingComponent.rankedScoresToMapLeaderboardDtos(scores);

        return ResponseEntity.ok(mapLeaderboardPlayerDtos);
    }
}
