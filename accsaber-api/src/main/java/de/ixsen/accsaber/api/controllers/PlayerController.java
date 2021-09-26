package de.ixsen.accsaber.api.controllers;

import de.ixsen.accsaber.api.dtos.PlayerDto;
import de.ixsen.accsaber.api.dtos.PlayerScoreDto;
import de.ixsen.accsaber.api.dtos.SignupDto;
import de.ixsen.accsaber.api.mapping.MappingComponent;
import de.ixsen.accsaber.business.PlayerService;
import de.ixsen.accsaber.business.RankedMapService;
import de.ixsen.accsaber.business.ScoreService;
import de.ixsen.accsaber.database.model.players.PlayerData;
import de.ixsen.accsaber.database.model.players.PlayerRankHistory;
import de.ixsen.accsaber.database.model.players.ScoreData;
import de.ixsen.accsaber.database.model.players.ScoreDataHistory;
import de.ixsen.accsaber.database.views.AccSaberPlayer;
import de.ixsen.accsaber.database.views.AccSaberScore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("players")
public class PlayerController {

    private final PlayerService playerService;
    private final MappingComponent mappingComponent;
    private final ScoreService scoreService;
    private final RankedMapService rankedMapService;

    @Autowired
    public PlayerController(PlayerService playerService, MappingComponent mappingComponent, ScoreService scoreService, RankedMapService rankedMapService) {
        this.playerService = playerService;
        this.mappingComponent = mappingComponent;
        this.scoreService = scoreService;
        this.rankedMapService = rankedMapService;
    }

    @PostMapping
    public ResponseEntity<?> addNewPlayer(@RequestBody SignupDto signupDto) {
        String playerId = signupDto.getScoresaberLink();
        if (playerId.toLowerCase().contains("scoresaber")) {
            playerId = playerId
                    .replace("https://scoresaber.com/u/", "")
                    .replace("http://scoresaber.com/u/", "")
                    .replace("scoresaber.com/u/", "")
                    .split("/")[0]
                    .split("\\?")[0];
        }

        this.playerService.signupPlayer(Long.parseLong(playerId), signupDto.getPlayerName(), signupDto.getHmd());
        return ResponseEntity.noContent().build();
    }

    @GetMapping(path = "/{playerId}")
    public ResponseEntity<PlayerDto> getPlayerInfo(@PathVariable long playerId) {
        Optional<AccSaberPlayer> accSaberPlayer = this.playerService.getRankedPlayer(playerId);
        PlayerDto playerDto;
        if (accSaberPlayer.isEmpty()) {
            PlayerData player = this.playerService.getPlayer(playerId);
            playerDto = this.mappingComponent.getPlayerMapper().rawPlayerToDto(player);
        } else {
            playerDto = this.mappingComponent.getPlayerMapper().playerToPlayerDto(accSaberPlayer.get());
        }

        return ResponseEntity.ok(playerDto);
    }

    @GetMapping(path = "/{playerId}/scores")
    public ResponseEntity<ArrayList<PlayerScoreDto>> getPlayerScores(@PathVariable Long playerId) {
        PlayerData player = this.playerService.getPlayer(playerId);
        List<AccSaberScore> scoresForPlayer = this.scoreService.getScoresForPlayer(player);
        ArrayList<PlayerScoreDto> playerScoreDtos = this.mappingComponent.getScoreMapper().rankedScoresToPlayerScores(scoresForPlayer);

        return ResponseEntity.ok(playerScoreDtos);
    }

    @GetMapping(path = "/{playerId}/score-history/{leaderboardId}")
    public ResponseEntity<Map<Instant, Double>> getPlayerScoreHistoryForMap(@PathVariable Long playerId, @PathVariable long leaderboardId) {
        PlayerData player = this.playerService.getPlayer(playerId);
        int maxScore = this.rankedMapService.getRankedMap(leaderboardId).getMaxScore();

        List<ScoreDataHistory> scoreHistoryForPlayer = this.scoreService.getScoreHistoryForPlayer(player, leaderboardId);
        Optional<ScoreData> currentScore = this.scoreService.getScoreByPlayerForLeaderboard(player, leaderboardId);

        Map<Instant, Double> map = scoreHistoryForPlayer
                .stream()
                .collect(Collectors.toMap(ScoreDataHistory::getTimeSet, score -> score.getScore() / (double) maxScore));
        currentScore.ifPresent(scoreData -> map.put(scoreData.getTimeSet(), scoreData.getScore() / (double) maxScore));

        return ResponseEntity.ok(map);
    }

    @GetMapping
    public ResponseEntity<ArrayList<PlayerDto>> getPlayers() {
        List<AccSaberPlayer> accSaberPlayerEntities = this.playerService.getAllPlayers();
        ArrayList<PlayerDto> playerDtos = this.mappingComponent.getPlayerMapper().playersToPlayerDtos(accSaberPlayerEntities);

        return ResponseEntity.ok(playerDtos);
    }

    @GetMapping(path = "/{playerId}/recent-rank-history")
    public ResponseEntity<Map<LocalDate, Integer>> getRecentPlayerRankHistory(@PathVariable Long playerId) {
        List<PlayerRankHistory> recentPlayerRankHistory = this.playerService.getRecentPlayerRankHistory(playerId);

        Map<LocalDate, Integer> map = recentPlayerRankHistory
                .stream()
                .collect(Collectors.toMap(PlayerRankHistory::getDate, PlayerRankHistory::getRanking));

        return ResponseEntity.ok(map);
    }

}
