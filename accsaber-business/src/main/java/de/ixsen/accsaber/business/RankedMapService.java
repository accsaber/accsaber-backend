package de.ixsen.accsaber.business;

import de.ixsen.accsaber.business.exceptions.AccsaberOperationException;
import de.ixsen.accsaber.business.exceptions.ExceptionType;
import de.ixsen.accsaber.database.model.maps.RankedMap;
import de.ixsen.accsaber.database.model.maps.Song;
import de.ixsen.accsaber.database.model.players.Score;
import de.ixsen.accsaber.database.repositories.RankedMapRepository;
import de.ixsen.accsaber.database.repositories.ScoreRepository;
import de.ixsen.accsaber.integration.connector.BeatSaverConnector;
import de.ixsen.accsaber.integration.model.beatsaver.BeatSaverDifficultyDetails;
import de.ixsen.accsaber.integration.model.beatsaver.BeatSaverSongInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
