package de.ixsen.accsaber.business;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.ixsen.accsaber.business.exceptions.AccsaberOperationException;
import de.ixsen.accsaber.business.exceptions.ExceptionType;
import de.ixsen.accsaber.business.playlist.Playlist;
import de.ixsen.accsaber.business.playlist.PlaylistSong;
import de.ixsen.accsaber.business.playlist.PlaylistSongDifficulty;
import de.ixsen.accsaber.database.model.maps.RankedMap;
import de.ixsen.accsaber.database.model.maps.Song;
import de.ixsen.accsaber.database.model.players.Score;
import de.ixsen.accsaber.database.repositories.RankedMapRepository;
import de.ixsen.accsaber.database.repositories.ScoreRepository;
import de.ixsen.accsaber.integration.connector.BeatSaverConnector;
import de.ixsen.accsaber.integration.model.beatsaver.BeatSaverDifficultyDetails;
import de.ixsen.accsaber.integration.model.beatsaver.BeatSaverSongInfo;
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
    private final RankedMapRepository rankedMapRepository;
    private final ScoreRepository scoreRepository;

    private final SongService songService;

    @Autowired
    public RankedMapService(RankedMapRepository rankedMapRepository,
                            ScoreRepository scoreRepository,
                            BeatSaverConnector beatSaverConnector,
                            SongService songService) {
        this.rankedMapRepository = rankedMapRepository;
        this.scoreRepository = scoreRepository;
        this.beatSaverConnector = beatSaverConnector;
        this.songService = songService;
    }

    public List<RankedMap> getRankedMaps() {
        return this.rankedMapRepository.findAll();
    }

    public RankedMap getRankedMap(Long leaderboardId) {
        Optional<RankedMap> optionalRankedMap = this.rankedMapRepository.findById(leaderboardId);
        if (optionalRankedMap.isEmpty()) {
            throw new AccsaberOperationException(ExceptionType.RANKED_MAP_NOT_FOUND, "The ranked map with the id could not be found.");
        }
        return optionalRankedMap.get();
    }

    public void addNewRankedMap(String beatSaverId, Long leaderBoardId, String difficulty, Double techyness) {
        BeatSaverSongInfo beatSaverSongInfo = this.beatSaverConnector.getMapInfoByKey(beatSaverId);

        if (this.rankedMapRepository.existsById(leaderBoardId)) {
            throw new AccsaberOperationException(ExceptionType.RANKED_MAP_ALREADY_EXISTS, "The ranked map with the leaderboardId " + leaderBoardId + " already exists");
        }

        Song song = this.songService.getOrCreateSong(beatSaverSongInfo);

        BeatSaverDifficultyDetails beatSaverDifficultyDetails = beatSaverSongInfo.getMetadata().getCharacteristics().get(0).getDifficulties().get(difficulty);

        RankedMap rankedMap = new RankedMap();
        rankedMap.setLeaderboardId(leaderBoardId);
        rankedMap.setMaxScore(this.calculateMaxScore(beatSaverDifficultyDetails.getNotes()));
        rankedMap.setSong(song);
        rankedMap.setTechyness(techyness);
        rankedMap.setDifficulty(difficulty);

        song.getRankedMaps().add(rankedMap);

        song = this.songService.saveSong(song);

        List<Score> nowRankedScores = this.scoreRepository.findAllByLeaderboardId(rankedMap.getLeaderboardId());
        nowRankedScores.forEach(score -> {
            score.setIsRankedMapScore(true);
            score.setAccuracy(score.getScore() / (double) rankedMap.getMaxScore());
            double ap = APUtils.calculateApByAcc(score.getAccuracy(), rankedMap.getTechyness());
            score.setAp(ap);
        });
        this.scoreRepository.saveAll(nowRankedScores);
    }

    public void removeRankedMap(Long leaderboardId) {
        if (this.rankedMapRepository.existsById(leaderboardId)) {
            RankedMap map = this.rankedMapRepository.getOne(leaderboardId);
            Song song = map.getSong();
            List<RankedMap> rankedMaps = song.getRankedMaps();
            rankedMaps.remove(map);
            song.setRankedMaps(rankedMaps);
            if (rankedMaps.size() == 0) {
                this.songService.removeSong(song.getSongHash());
            }
            this.rankedMapRepository.deleteById(leaderboardId);
        }
    }

    public byte[] getRankedMapsJson() throws JsonProcessingException {
        Playlist playlist = new Playlist();
        playlist.setPlaylistTitle("AccSaber Ranked Maps");
        playlist.setPlaylistAuthor("AccSaber");
        playlist.setImage(this.getPlaylistImage());

        for (RankedMap rankedMap : this.getRankedMaps()) {
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

    // There has to be a better way to do this
    private int calculateMaxScore(int noteCount) {
        int score = 0;
        for (int i = 1; i <= noteCount; i++) {

            if (i <= 1) {
                score += 1;
            } else if (i <= 5) {
                score += 2;
            } else if (i <= 13) {
                score += 4;
            } else {
                break;
            }
        }
        score *= 115;
        if (noteCount > 13) {
            score += (8 * (noteCount - 13)) * 115;
        }

        return score;
    }


}
