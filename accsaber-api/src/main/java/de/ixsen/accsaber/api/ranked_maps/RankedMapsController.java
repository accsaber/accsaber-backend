package de.ixsen.accsaber.api.ranked_maps;

import com.fasterxml.jackson.core.JsonProcessingException;
import de.ixsen.accsaber.business.maps.RankedMapService;
import de.ixsen.accsaber.business.players.PlayerService;
import de.ixsen.accsaber.database.model.maps.BeatMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/ranked-maps")
public class RankedMapsController {

    private final RankedMapService rankedMapService;
    private final PlayerService playerService;
    private final RankedMapMapper rankedMapMapper;

    @Autowired
    public RankedMapsController(RankedMapService rankedMapService, PlayerService playerService, RankedMapMapper rankedMapMapper) {
        this.rankedMapService = rankedMapService;
        this.playerService = playerService;

        this.rankedMapMapper = rankedMapMapper;
    }

    @GetMapping
    public ResponseEntity<List<RankedMapDto>> getAllRankedMaps() {
        List<RankedMapDto> rankedMapDtos = this.rankedMapMapper.rankedMapsToDtos(this.rankedMapService.getRankedMaps());
        return ResponseEntity.ok(rankedMapDtos);
    }

    @GetMapping("/statistics")
    public ResponseEntity<RankedMapsStatisticsDto> getRankedStat() {
        List<BeatMap> rankedMaps = this.rankedMapService.getRankedMaps();
        long trueAccCount = rankedMaps.stream().filter(r -> r.getComplexity() <= 4).count();
        long techAccCount = rankedMaps.stream().filter(r -> r.getComplexity() >= 10).count();
        RankedMapsStatisticsDto rankedMapsStatisticsDto = new RankedMapsStatisticsDto();
        rankedMapsStatisticsDto.setMapCount(rankedMaps.size());
        rankedMapsStatisticsDto.setTechAccMapCount(techAccCount);
        rankedMapsStatisticsDto.setTrueAccMapCount(trueAccCount);
        rankedMapsStatisticsDto.setStandardAccMapCount(rankedMaps.size() - trueAccCount - techAccCount);
        Map<Double, Long> complexityToMapCount = rankedMaps
                .stream()
                .collect(Collectors.groupingBy(BeatMap::getComplexity, Collectors.counting()));
        rankedMapsStatisticsDto.setComplexityToMapCount(complexityToMapCount);

        return ResponseEntity.ok(rankedMapsStatisticsDto);
    }

    @GetMapping("/{leaderboardId}")
    public ResponseEntity<RankedMapDto> getRankedMapDetails(@PathVariable Long leaderboardId) {
        BeatMap rankedMap = this.rankedMapService.getRankedMap(leaderboardId);
        RankedMapDto rankedMapDto = this.rankedMapMapper.rankedMapToDto(rankedMap);
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
