package de.ixsen.accsaber.business;

import de.ixsen.accsaber.database.model.maps.Song;
import de.ixsen.accsaber.database.repositories.model.SongRepository;
import de.ixsen.accsaber.integration.connector.ScoreSaberConnector;
import de.ixsen.accsaber.integration.model.beatsaver.BeatSaverSongInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Optional;

@Service
public class SongService implements HasLogger {

    private final SongRepository songRepository;
    private final ScoreSaberConnector scoreSaberConnector;

    private final String coverFolder;

    public SongService(SongRepository songRepository, ScoreSaberConnector scoreSaberConnector,
                       @Value("${accsaber.image-save-location}") String imageFolder) {
        this.songRepository = songRepository;
        this.scoreSaberConnector = scoreSaberConnector;

        this.coverFolder = imageFolder + "/covers";
    }

    public Song saveSong(Song song) {
        return this.songRepository.save(song);
    }

    public Song getOrCreateSong(BeatSaverSongInfo beatSaverSongInfo) {
        Optional<Song> optionalSong = this.songRepository.findById(beatSaverSongInfo.getHash());
        return optionalSong.orElseGet(() -> this.createSong(beatSaverSongInfo));
    }

    public void removeSong(String hash) {
        this.songRepository.deleteById(hash);
        this.deleteSongCover(hash);
    }

    public void reloadAllCovers() {
        Instant start = Instant.now();
        this.getLogger().info("Reloading all song covers");
        this.songRepository.findAll().forEach(song -> this.saveSongCover(song.getSongHash()));
        this.getLogger().info("Loading song covers finished in {} seconds.", Duration.between(start, Instant.now()).getSeconds());
    }

    private Song createSong(BeatSaverSongInfo beatSaverSongInfo) {
        Song song = new Song();
        song.setLevelAuthorName(beatSaverSongInfo.getMetadata().getLevelAuthorName());
        song.setSongAuthorName(beatSaverSongInfo.getMetadata().getSongAuthorName());
        song.setSongHash(beatSaverSongInfo.getHash());
        song.setSongName(beatSaverSongInfo.getMetadata().getSongName());
        song.setSongSubName(beatSaverSongInfo.getMetadata().getSongSubName());
        song.setBeatSaverKey(beatSaverSongInfo.getKey());

        song.setBeatMaps(new ArrayList<>());

        this.saveSongCover(beatSaverSongInfo.getHash());

        return this.songRepository.save(song);
    }

    private void saveSongCover(String hash) {
        byte[] cover = this.scoreSaberConnector.loadCover(hash);
        try (FileOutputStream fileOutputStream = new FileOutputStream(this.coverFolder + "/" + hash.toUpperCase() + ".png")) {
            fileOutputStream.write(cover);
        } catch (IOException e) {
            this.getLogger().error("Unable to save cover for song with hash {}", hash, e);
        }
    }

    private void deleteSongCover(String hash) {
        File file = new File(this.coverFolder + "/" + hash.toUpperCase() + ".png");
        boolean deleted = file.delete();
        if (!deleted) {
            this.getLogger().error("Failed to delete cover photo.");
        }
    }
}

// The update includes song covers and avatars as well as a general layout change