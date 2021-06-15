package de.ixsen.accsaber.api.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import de.ixsen.accsaber.api.dtos.CreateRankedMapDto;
import de.ixsen.accsaber.api.dtos.RankedMapDto;
import de.ixsen.accsaber.api.dtos.RankedMapsStatisticsDto;
import de.ixsen.accsaber.api.dtos.RemoveRankedMapDto;
import de.ixsen.accsaber.api.mapping.MappingComponent;
import de.ixsen.accsaber.business.PlayerService;
import de.ixsen.accsaber.business.RankedMapService;
import de.ixsen.accsaber.database.model.maps.RankedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
        this.rankedMapService.addNewRankedMap(rankedMapDto.getBeatSaverKey(), rankedMapDto.getLeaderboardId(), rankedMapDto.getDifficulty(), rankedMapDto.getTechyness());
        this.playerService.recalculateApForAllPlayers();
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<RankedMapDto>> getAllRankedMaps() {
        List<RankedMapDto> rankedMapDtos = this.mappingComponent
                .getRankedMapMapper().rankedMapsToDtos(this.rankedMapService.getRankedMaps());
        return ResponseEntity.ok(rankedMapDtos);
    }

    @PostMapping("/delete")
    public ResponseEntity<?> removeRankedMap(@RequestBody RemoveRankedMapDto removeRankedMapDto) {
        this.rankedMapService.removeRankedMap(removeRankedMapDto.getLeaderboardId(), removeRankedMapDto.getBeatSaverKey());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/statistics")
    public ResponseEntity<RankedMapsStatisticsDto> getRankedStat() {
        List<RankedMap> rankedMaps = this.rankedMapService.getRankedMaps();
        long trueAccCount = rankedMaps.stream().filter(r -> r.getTechyness() <= 4).count();
        long techAccCount = rankedMaps.stream().filter(r -> r.getTechyness() >= 10).count();
        RankedMapsStatisticsDto rankedMapsStatisticsDto = new RankedMapsStatisticsDto();
        rankedMapsStatisticsDto.setMapCount(rankedMaps.size());
        rankedMapsStatisticsDto.setTechAccMapCount(techAccCount);
        rankedMapsStatisticsDto.setTrueAccMapCount(trueAccCount);
        rankedMapsStatisticsDto.setStandardAccMapCount(rankedMaps.size() - trueAccCount - techAccCount);
        Map<Double, Long> techynessToMapCount = rankedMaps
                .stream()
                .collect(Collectors.groupingBy(RankedMap::getTechyness, Collectors.counting()));
        rankedMapsStatisticsDto.setTechynessToMapCount(techynessToMapCount);

        return ResponseEntity.ok(rankedMapsStatisticsDto);
    }

    @GetMapping("/{leaderboardId}")
    public ResponseEntity<RankedMapDto> getRankedMapDetails(@PathVariable Long leaderboardId) {
        RankedMap rankedMap = this.rankedMapService.getRankedMap(leaderboardId);
        RankedMapDto rankedMapDto = this.mappingComponent.getRankedMapMapper().rankedMapToDto(rankedMap);
        return ResponseEntity.ok(rankedMapDto);
    }

    @GetMapping("/playlist")
    public ResponseEntity<byte[]> getRankedMapPlaylist() throws JsonProcessingException {
        byte[] playlistJson = this.rankedMapService.getRankedMapsJson();
        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=accsaber-rankedmaps.json")
                .body(playlistJson);
    }
}
