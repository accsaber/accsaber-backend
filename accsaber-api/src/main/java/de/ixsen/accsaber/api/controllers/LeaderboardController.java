package de.ixsen.accsaber.api.controllers;

import de.ixsen.accsaber.api.dtos.LeaderboardDto;
import de.ixsen.accsaber.api.mapping.LeaderboardMapper;
import de.ixsen.accsaber.business.LeaderboardService;
import de.ixsen.accsaber.database.model.Leaderboard;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("map-leaderboards")
public class LeaderboardController {

    private final LeaderboardService leaderboardService;
    private final LeaderboardMapper leaderboardMapper;

    @Autowired
    public LeaderboardController(LeaderboardService leaderboardService, LeaderboardMapper leaderboardMapper) {
        this.leaderboardService = leaderboardService;
        this.leaderboardMapper = leaderboardMapper;
    }

    @GetMapping
    public ResponseEntity<List<LeaderboardDto>> getLeaderboards() {
        List<Leaderboard> allLeaderboards = this.leaderboardService.getAllLeaderboards();
        List<LeaderboardDto> leaderboardDtos = this.leaderboardMapper.map(allLeaderboards);
        return ResponseEntity.ok(leaderboardDtos);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createNewLeaderboard(LeaderboardDto leaderboardDto) {
        Leaderboard newLeaderboard = this.leaderboardMapper.map(leaderboardDto);
        this.leaderboardService.createNewLeaderboard(newLeaderboard);
    }

    @DeleteMapping("{leaderboardName}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteLeaderboard(@PathVariable String leaderboardName) {
        this.leaderboardService.deleteLeaderboard(leaderboardName);
    }

    @PutMapping("{leaderboardName}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateLeaderboardDescription(@PathVariable String leaderboardName, @RequestBody LeaderboardDto leaderboardDto) {
        this.leaderboardService.updateDescription(leaderboardName, leaderboardDto.getDescription());
    }
}
