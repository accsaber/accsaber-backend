package de.ixsen.accsaber.api.controllers;

import de.ixsen.accsaber.api.dtos.CreateRankedMapDto;
import de.ixsen.accsaber.api.dtos.RankedMapDto;
import de.ixsen.accsaber.api.mapping.MappingComponent;
import de.ixsen.accsaber.business.PlayerService;
import de.ixsen.accsaber.business.RankedMapService;
import de.ixsen.accsaber.database.model.maps.RankedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/ranked-maps")
public class RankedMapsController {

    private final RankedMapService rankedMapService;
    private final PlayerService playerService;
    private final MappingComponent mappingComponent;

    @Autowired
    public RankedMapsController(RankedMapService rankedMapService, PlayerService playerService, MappingComponent mappingComponent) {
        this.rankedMapService = rankedMapService;
        this.playerService = playerService;

        this.mappingComponent = mappingComponent;
    }

    @PostMapping
    public ResponseEntity<?> addNewRankedMap(@RequestBody CreateRankedMapDto rankedMapDto) {
        this.rankedMapService.addNewRankedMap(rankedMapDto.getBeatSaverId(), rankedMapDto.getLeaderBoardId(), rankedMapDto.getDifficulty(), rankedMapDto.getTechyness());
        this.playerService.recalculateApForAllPlayers();
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<RankedMapDto>> getAllRankedMaps() {
        List<RankedMapDto> rankedMapDtos = this.mappingComponent
                .getRankedMapMapper().rankedMapsToDtos(this.rankedMapService.getRankedSongs());
        return ResponseEntity.ok(rankedMapDtos);
    }

    @GetMapping("/{leaderboardId}")
    public ResponseEntity<RankedMapDto> getRankedMapDetails(@PathVariable Long leaderboardId) {
        RankedMap rankedMap = this.rankedMapService.getRankedMap(leaderboardId);
        RankedMapDto rankedMapDto = this.mappingComponent.getRankedMapMapper().rankedMapToDto(rankedMap);
        return ResponseEntity.ok(rankedMapDto);
    }
}
