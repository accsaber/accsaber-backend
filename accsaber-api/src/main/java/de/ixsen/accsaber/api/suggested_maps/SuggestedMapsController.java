package de.ixsen.accsaber.api.suggested_maps;

import de.ixsen.accsaber.business.maps.SuggestedMapService;
import de.ixsen.accsaber.business.staff.StaffService;
import de.ixsen.accsaber.database.model.maps.BeatMap;
import de.ixsen.accsaber.database.model.staff.StaffUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/suggested-maps")
public class SuggestedMapsController {

    private final SuggestedMapService suggestedMapService;
    private final StaffService staffService;
    private final SuggestedMapMapper suggestedMapMapper;

    @Autowired
    public SuggestedMapsController(SuggestedMapService suggestedMapService, StaffService staffService, SuggestedMapMapper suggestedMapMapper) {
        this.suggestedMapService = suggestedMapService;
        this.staffService = staffService;
        this.suggestedMapMapper = suggestedMapMapper;
    }

    @GetMapping
    public ResponseEntity<List<SuggestedMapDto>> getAllSuggestedMaps() {
        List<BeatMap> allSuggestedMaps = this.suggestedMapService.getAllSuggestedMaps();
        return ResponseEntity.ok(this.suggestedMapMapper.mapListToDtos(allSuggestedMaps));
    }

    @PostMapping
    public ResponseEntity<Void> suggestNewMap(@RequestBody MapSuggestionDto mapSuggestionDto) {
        StaffUser staffUser = this.staffService.findUserByName(SecurityContextHolder.getContext().getAuthentication().getName());
        this.suggestedMapService.addNewMapSuggestion(
                mapSuggestionDto.getBeatSaverKey(),
                mapSuggestionDto.getLeaderboardId(),
                mapSuggestionDto.getDifficulty(),
                mapSuggestionDto.getComplexity(),
                staffUser);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("{leaderboardId}")
    public ResponseEntity<Void> voteOnMap(@PathVariable long leaderboardId, @RequestParam int direction) {
        StaffUser staffUser = this.staffService.findUserByName(SecurityContextHolder.getContext().getAuthentication().getName());
        this.suggestedMapService.voteOnMap(leaderboardId, direction, staffUser);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("{leaderboardId}")
    public ResponseEntity<?> qualifyMap(@PathVariable long leaderboardId) {
        this.suggestedMapService.qualifyMap(leaderboardId);
        return ResponseEntity.noContent().build();
    }
}
