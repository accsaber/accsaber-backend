package de.ixsen.accsaber.business;

import de.ixsen.accsaber.business.exceptions.AccsaberOperationException;
import de.ixsen.accsaber.business.exceptions.ExceptionType;
import de.ixsen.accsaber.database.model.maps.RankedMap;
import de.ixsen.accsaber.database.model.maps.Song;
import de.ixsen.accsaber.database.model.players.Score;
import de.ixsen.accsaber.database.repositories.RankedMapRepository;
import de.ixsen.accsaber.database.repositories.ScoreRepository;
import de.ixsen.accsaber.database.repositories.SongRepository;
import de.ixsen.accsaber.integration.connector.BeatSaverConnector;
import de.ixsen.accsaber.integration.model.beatsaver.BeatSaverDifficultyDetails;
import de.ixsen.accsaber.integration.model.beatsaver.BeatSaverSongInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RankedMapService {

    private final SongRepository songRepository;
    private final BeatSaverConnector beatSaverConnector;
    private final RankedMapRepository rankedMapRepository;
    private final ScoreRepository scoreRepository;

    @Autowired
    public RankedMapService(SongRepository songRepository,
                            RankedMapRepository rankedMapRepository,
                            ScoreRepository scoreRepository,
                            BeatSaverConnector beatSaverConnector) {
        this.songRepository = songRepository;
        this.rankedMapRepository = rankedMapRepository;
        this.scoreRepository = scoreRepository;
        this.beatSaverConnector = beatSaverConnector;
    }

    public List<RankedMap> getRankedSongs() {
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
        Optional<Song> optionalSong = this.songRepository.findById(beatSaverSongInfo.getHash());
        Song song = optionalSong.orElseGet(() -> this.createSong(beatSaverSongInfo));

        if (this.rankedMapRepository.existsById(leaderBoardId)) {
            throw new AccsaberOperationException(ExceptionType.RANKED_MAP_ALREADY_EXISTS, "The ranked map with the leaderboardId " + leaderBoardId + " already exists");
        }

        BeatSaverDifficultyDetails beatSaverDifficultyDetails = beatSaverSongInfo.getMetadata().getCharacteristics().get(0).getDifficulties().get(difficulty);

        RankedMap rankedMap = new RankedMap();
        rankedMap.setLeaderboardId(leaderBoardId);
        rankedMap.setMaxScore(this.calculateMaxScore(beatSaverDifficultyDetails.getNotes()));
        rankedMap.setSong(song);
        rankedMap.setTechyness(techyness);
        rankedMap.setDifficulty(difficulty);

        song.getRankedMaps().add(rankedMap);

        this.rankedMapRepository.save(rankedMap);

        List<Score> nowRankedScores = this.scoreRepository.findAllByLeaderboardId(rankedMap.getLeaderboardId());
        nowRankedScores.forEach(score -> {
            score.setIsRankedMapScore(true);
            score.setAccuracy(score.getScore() / (double) rankedMap.getMaxScore());
            double ap = APUtils.calculateApByAcc(score.getAccuracy(), rankedMap.getTechyness());
            score.setAp(ap);
        });
        this.scoreRepository.saveAll(nowRankedScores);
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

    private Song createSong(BeatSaverSongInfo beatSaverSongInfo) {
        Song song = new Song();
        song.setLevelAuthorName(beatSaverSongInfo.getMetadata().getLevelAuthorName());
        song.setSongAuthorName(beatSaverSongInfo.getMetadata().getSongAuthorName());
        song.setSongHash(beatSaverSongInfo.getHash());
        song.setSongName(beatSaverSongInfo.getName());
        song.setSongSubName(beatSaverSongInfo.getMetadata().getSongSubName());
        song.setBeatSaverKey(beatSaverSongInfo.getKey());

        song.setRankedMaps(new ArrayList<>());

        return song;
    }
}
