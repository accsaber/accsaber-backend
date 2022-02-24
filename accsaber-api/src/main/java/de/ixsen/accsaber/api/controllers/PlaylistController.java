package de.ixsen.accsaber.api.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import de.ixsen.accsaber.api.dtos.PlaylistInfoDto;
import de.ixsen.accsaber.api.mapping.PlaylistInfoMapper;
import de.ixsen.accsaber.business.CategoryService;
import de.ixsen.accsaber.business.PlaylistService;
import de.ixsen.accsaber.database.model.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("playlists")
public class PlaylistController {

    private final PlaylistService playlistService;
    private final CategoryService categoryService;
    private final PlaylistInfoMapper playlistInfoMapper;


    @Autowired
    public PlaylistController(PlaylistService playlistService, CategoryService categoryService, PlaylistInfoMapper playlistInfoMapper) {
        this.playlistService = playlistService;
        this.categoryService = categoryService;
        this.playlistInfoMapper = playlistInfoMapper;
    }

    @GetMapping
    public ResponseEntity<ArrayList<PlaylistInfoDto>> getPlaylists() {
        List<Category> allCategories = this.categoryService.getAllCategoriesIncludingJointCategories();
        ArrayList<PlaylistInfoDto> playlistInfoDtos = this.playlistInfoMapper.map(allCategories);

        return ResponseEntity.ok(playlistInfoDtos);
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
