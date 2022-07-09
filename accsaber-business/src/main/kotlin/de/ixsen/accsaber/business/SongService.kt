package de.ixsen.accsaber.business

import de.ixsen.accsaber.database.model.maps.Song
import de.ixsen.accsaber.database.repositories.model.SongRepository
import de.ixsen.accsaber.integration.connector.BeatSaverConnector
import de.ixsen.accsaber.integration.model.beatsaver.BeatSaverSongInfo
import de.ixsen.accsaber.integration.model.beatsaver.BeatSaverVersion
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.time.Duration
import java.time.Instant
import java.util.*
import java.util.function.Consumer
import kotlin.collections.ArrayList

@Service
class SongService(
    private val songRepository: SongRepository, private val beatSaverConnector: BeatSaverConnector,
    @Value("\${accsaber.image-save-location}") imageFolder: String
) : HasLogger {
    private val coverFolder: String

    init {
        coverFolder = "$imageFolder/covers"
    }

    fun saveSong(song: Song): Song {
        return songRepository.save(song)
    }

    fun getOrCreateSong(beatSaverSongInfo: BeatSaverSongInfo, beatSaverVersion: BeatSaverVersion): Song {
        val optionalSong = songRepository.findById(beatSaverVersion.hash!!)
        return optionalSong.orElseGet { createSong(beatSaverSongInfo, beatSaverVersion) }
    }

    fun removeSong(hash: String) {
        songRepository.deleteById(hash)
        deleteSongCover(hash)
    }

    fun reloadAllCovers() {
        val start = Instant.now()
        this.getLogger().info("Reloading all song covers")
        songRepository.findAll().forEach(Consumer { song: Song -> saveSongCover(song.songHash) })
        this.getLogger().info("Loading song covers finished in {} seconds.", Duration.between(start, Instant.now()).seconds)
    }

    private fun createSong(beatSaverSongInfo: BeatSaverSongInfo, beatSaverVersion: BeatSaverVersion): Song {
        val song = Song(
            beatSaverVersion.hash!!,
            beatSaverSongInfo.metadata?.songName!!,
            beatSaverSongInfo.metadata?.songSubName!!,
            beatSaverSongInfo.metadata?.songAuthorName!!,
            beatSaverSongInfo.metadata?.levelAuthorName!!,
            beatSaverSongInfo.id!!
        )

        saveSongCover(beatSaverVersion.hash!!)
        return songRepository.save(song)
    }

    private fun saveSongCover(hash: String) {
        val cover = beatSaverConnector.loadCover(hash)
        try {
            FileOutputStream(coverFolder + "/" + hash.uppercase(Locale.getDefault()) + ".png").use { fileOutputStream -> fileOutputStream.write(cover) }
        } catch (e: IOException) {
            this.getLogger().error("Unable to save cover for song with hash {}", hash, e)
        }
    }

    private fun deleteSongCover(hash: String) {
        val file = File(coverFolder + "/" + hash.uppercase(Locale.getDefault()) + ".png")
        val deleted = file.delete()
        if (!deleted) {
            this.getLogger().error("Failed to delete cover photo.")
        }
    }
}