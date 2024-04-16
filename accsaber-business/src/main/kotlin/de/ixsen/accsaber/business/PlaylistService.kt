package de.ixsen.accsaber.business

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import de.ixsen.accsaber.business.exceptions.AccsaberOperationException
import de.ixsen.accsaber.business.exceptions.ExceptionType
import de.ixsen.accsaber.business.playlist.Playlist
import de.ixsen.accsaber.business.playlist.PlaylistSong
import de.ixsen.accsaber.business.playlist.PlaylistSongDifficulty
import de.ixsen.accsaber.database.model.maps.BeatMap
import de.ixsen.accsaber.database.repositories.model.BeatMapRepository
import de.ixsen.accsaber.database.repositories.model.CategoryRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Service
import org.springframework.util.FileCopyUtils
import java.io.File
import java.io.IOException
import java.io.InputStreamReader
import java.io.UncheckedIOException
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.util.*

@Service
class PlaylistService @Autowired constructor(
    private val beatMapRepository: BeatMapRepository,
    private val categoryRepository: CategoryRepository,
    @param:Value("\${accsaber.playlist-url}") private val playlistUrl: String,
    @param:Value("\${accsaber.image-save-location}") private val imageSaveLocation: String
) {
    @Throws(JsonProcessingException::class)
    fun getPlaylist(categoryName: String): ByteArray {
        if (categoryName == "all") {
            return getRankedMapsJson(beatMapRepository.findAll(), categoryName, categoryName)
        }
        if (categoryName == "overall") {
            return getRankedMapsJson(beatMapRepository.findAllCountingTowardsOverall(), categoryName, categoryName)
        }
        val category = categoryRepository.findByCategoryName(categoryName)
            .orElseThrow { AccsaberOperationException(ExceptionType.CATEGORY_NOT_FOUND, String.format("The category [%s] was not found.", categoryName)) }
        return getRankedMapsJson(beatMapRepository.findAllForCategory(category), categoryName, category.categoryDisplayName)
    }

    @Throws(JsonProcessingException::class)
    private fun getRankedMapsJson(maps: List<BeatMap>, category: String, categoryDisplayName: String): ByteArray {
        val playlist = Playlist(
            "AccSaber ${categoryDisplayName.capitalise()} Ranked Maps",
            "AccSaber",
            getPlaylistImageData(category),
            playlistUrl + category,
        )

        for (beatMap in maps) {
            val playlistSong = playlist.songs.stream()
                .filter { s: PlaylistSong -> s.hash == beatMap.song.songHash }
                .findFirst().orElseGet {
                    val newPlaylistSong = PlaylistSong(
                        beatMap.song.songHash, beatMap.song.songName,
                    )
                    playlist.songs.add(newPlaylistSong)
                    newPlaylistSong
                }
            val playlistSongDifficulty = PlaylistSongDifficulty()
            playlistSongDifficulty.name = beatMap.difficulty
            playlistSongDifficulty.characteristic = "Standard"
            playlistSong.difficulties.add(playlistSongDifficulty)
        }
        val mapper = ObjectMapper()
        return mapper.writeValueAsBytes(playlist)
    }

    private fun getPlaylistImageData(category: String): String {
        val categoryFilePath = String.format("%s/playlists/%s.png", imageSaveLocation, category)
        val file = File(categoryFilePath)
        if (!file.exists()) {
            val resource = ClassPathResource("logo-data")
            try {
                InputStreamReader(resource.inputStream, StandardCharsets.UTF_8).use { reader -> return FileCopyUtils.copyToString(reader) }
            } catch (e: IOException) {
                throw UncheckedIOException(e) // TODO
            }
        }
        return try {
            "data:image/png;base64," + Base64.getEncoder().encodeToString(Files.readAllBytes(file.toPath()))
        } catch (e: IOException) {
            throw UncheckedIOException(e) // TODO
        }
    }
}