package de.ixsen.accsaber.business.maps;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.ixsen.accsaber.business.SongService;
import de.ixsen.accsaber.business.exceptions.AccsaberOperationException;
import de.ixsen.accsaber.business.exceptions.ExceptionType;
import de.ixsen.accsaber.business.playlist.Playlist;
import de.ixsen.accsaber.business.playlist.PlaylistSong;
import de.ixsen.accsaber.business.playlist.PlaylistSongDifficulty;
import de.ixsen.accsaber.database.model.maps.BeatMap;
import de.ixsen.accsaber.database.repositories.BeatMapRepository;
import de.ixsen.accsaber.database.repositories.ScoreRepository;
import de.ixsen.accsaber.integration.connector.BeatSaverConnector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.util.List;
import java.util.Optional;

import static java.nio.charset.StandardCharsets.UTF_8;

@Service
public class RankedMapService {

    private final BeatSaverConnector beatSaverConnector;
    private final BeatMapRepository mapRepository;
    private final ScoreRepository scoreRepository;

    private final SongService songService;

    @Autowired
    public RankedMapService(BeatMapRepository mapRepository,
                            ScoreRepository scoreRepository,
                            BeatSaverConnector beatSaverConnector,
                            SongService songService) {
        this.mapRepository = mapRepository;
        this.scoreRepository = scoreRepository;
        this.beatSaverConnector = beatSaverConnector;
        this.songService = songService;
    }

    public List<BeatMap> getRankedMaps() {
        return this.mapRepository.findAll();
    }

    public BeatMap getRankedMap(Long leaderboardId) {
        Optional<BeatMap> optionalRankedMap = this.mapRepository.findById(leaderboardId);
        if (optionalRankedMap.isEmpty()) {
            throw new AccsaberOperationException(ExceptionType.RANKED_MAP_NOT_FOUND, "The ranked map with the id could not be found.");
        }
        return optionalRankedMap.get();
    }

    public byte[] getRankedMapsJson() throws JsonProcessingException {
        Playlist playlist = new Playlist();
        playlist.setPlaylistTitle("AccSaber Ranked Maps");
        playlist.setPlaylistAuthor("AccSaber");
        playlist.setImage(this.getPlaylistImage());

        for (BeatMap rankedMap : this.getRankedMaps()) {
            PlaylistSong playlistSong = playlist.getSongs().stream().filter(s -> s.getHash().equals(rankedMap.getSong().getSongHash())).findFirst().orElseGet(() -> {
                PlaylistSong newPlaylistSong = new PlaylistSong();
                newPlaylistSong.setHash(rankedMap.getSong().getSongHash());
                newPlaylistSong.setSongName(rankedMap.getSong().getSongName());

                playlist.getSongs().add(newPlaylistSong);
                return newPlaylistSong;
            });
            PlaylistSongDifficulty playlistSongDifficulty = new PlaylistSongDifficulty();
            playlistSongDifficulty.setName(rankedMap.getDifficulty());
            playlistSongDifficulty.setCharacteristic("Standard");
            playlistSong.getDifficulties().add(playlistSongDifficulty);
        }
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsBytes(playlist);
    }

    private String getPlaylistImage() {
        ClassPathResource resource = new ClassPathResource("logo-data");
        try (Reader reader = new InputStreamReader(resource.getInputStream(), UTF_8)) {
            return FileCopyUtils.copyToString(reader);
        } catch (IOException e) {
            throw new UncheckedIOException(e); // TODO
        }
    }


}
