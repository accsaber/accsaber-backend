package de.ixsen.accsaber.api.controllers;

import de.ixsen.accsaber.api.dtos.PlayerDto;
import de.ixsen.accsaber.api.dtos.PlayerScoreDto;
import de.ixsen.accsaber.api.dtos.SignupDto;
import de.ixsen.accsaber.api.mapping.MappingComponent;
import de.ixsen.accsaber.business.PlayerService;
import de.ixsen.accsaber.business.ScoreService;
import de.ixsen.accsaber.database.model.players.Player;
import de.ixsen.accsaber.database.model.players.RankedPlayer;
import de.ixsen.accsaber.database.model.players.RankedScore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("players")
public class PlayerController {

    private final PlayerService playerService;
    private final MappingComponent mappingComponent;
    private final ScoreService scoreService;

    @Autowired
    public PlayerController(PlayerService playerService, MappingComponent mappingComponent, ScoreService scoreRepository) {
        this.playerService = playerService;
        this.mappingComponent = mappingComponent;
        this.scoreService = scoreRepository;
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

        this.playerService.signupPlayer(playerId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(path = "/{playerId}")
    public ResponseEntity<PlayerDto> getPlayerInfo(@PathVariable String playerId) {
        RankedPlayer player = this.playerService.getRankedPlayer(playerId);
        PlayerDto playerDto = this.mappingComponent.getPlayerMapper().playerToPlayerDto(player);

        return ResponseEntity.ok(playerDto);
    }

    @GetMapping(path = "/{playerId}/scores")
    public ResponseEntity<ArrayList<PlayerScoreDto>> getPlayerScores(@PathVariable String playerId) {
        Player player = this.playerService.getPlayer(playerId);
        List<RankedScore> scoresForPlayer = this.scoreService.getScoresForPlayer(player);
        ArrayList<PlayerScoreDto> playerScoreDtos = this.mappingComponent.getScoreMapper().rankedScoresToPlayerScores(scoresForPlayer);

        return ResponseEntity.ok(playerScoreDtos);
    }

    @GetMapping
    public ResponseEntity<ArrayList<PlayerDto>> getPlayers() {
        List<RankedPlayer> playerEntities = this.playerService.getAllPlayers();
        ArrayList<PlayerDto> playerDtos = this.mappingComponent.getPlayerMapper().playersToPlayerDtos(playerEntities);

        return ResponseEntity.ok(playerDtos);
    }


}
