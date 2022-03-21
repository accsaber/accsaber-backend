package de.ixsen.accsaber.api.controllers

import com.fasterxml.jackson.core.JsonProcessingException
import de.ixsen.accsaber.api.dtos.PlaylistInfoDto
import de.ixsen.accsaber.api.mapping.PlaylistInfoMapper
import de.ixsen.accsaber.business.CategoryService
import de.ixsen.accsaber.business.PlaylistService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("playlists")
class PlaylistController @Autowired constructor(
    private val playlistService: PlaylistService,
    private val categoryService: CategoryService,
    private val playlistInfoMapper: PlaylistInfoMapper
) {
    @get:GetMapping
    val playlists: ResponseEntity<ArrayList<PlaylistInfoDto>>
        get() {
            val allCategories = categoryService.getAllCategoriesIncludingJointCategories()
            val playlistInfoDtos = playlistInfoMapper.map(allCategories)
            return ResponseEntity.ok(playlistInfoDtos)
        }

    @GetMapping("{categoryName}")
    @Throws(JsonProcessingException::class)
    fun getRankedMapPlaylist(@PathVariable categoryName: String): ResponseEntity<ByteArray> {
        val playlistJson = playlistService.getPlaylist(categoryName)
        return ResponseEntity
            .ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, String.format("attachment; filename=accsaber-rankedmaps-%s.json", categoryName))
            .body(playlistJson)
    }
}