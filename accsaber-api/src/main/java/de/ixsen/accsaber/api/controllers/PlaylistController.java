package de.ixsen.accsaber.api.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import de.ixsen.accsaber.business.PlaylistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("playlists")
public class PlaylistController {

    private final PlaylistService playlistService;

    @Autowired
    public PlaylistController(PlaylistService playlistService) {
        this.playlistService = playlistService;
    }

    @GetMapping("{categoryName}")
    public ResponseEntity<byte[]> getRankedMapPlaylist(@PathVariable String categoryName) throws JsonProcessingException {
        byte[] playlistJson = this.playlistService.getPlaylist(categoryName);
        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, String.format("attachment; filename=accsaber-rankedmaps-%s.json", categoryName))
                .body(playlistJson);
    }
}
