package de.ixsen.accsaber.business.maps;

import de.ixsen.accsaber.business.SongService;
import de.ixsen.accsaber.business.exceptions.AccsaberOperationException;
import de.ixsen.accsaber.business.exceptions.ExceptionType;
import de.ixsen.accsaber.database.model.maps.MapSuggestionVote;
import de.ixsen.accsaber.database.model.maps.BeatMap;
import de.ixsen.accsaber.database.model.maps.RankedStage;
import de.ixsen.accsaber.database.model.maps.Song;
import de.ixsen.accsaber.database.model.staff.StaffUser;
import de.ixsen.accsaber.database.repositories.BeatMapRepository;
import de.ixsen.accsaber.database.repositories.MapSuggestionRepository;
import de.ixsen.accsaber.integration.connector.BeatSaverConnector;
import de.ixsen.accsaber.integration.model.beatsaver.BeatSaverDifficultyDetails;
import de.ixsen.accsaber.integration.model.beatsaver.BeatSaverSongInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SuggestedMapService {

    private final BeatMapRepository beatMapRepository;
    private final BeatSaverConnector beatSaverConnector;
    private final SongService songService;
    private final MapSuggestionRepository mapSuggestionRepository;

    @Autowired
    public SuggestedMapService(BeatMapRepository beatMapRepository,
                               BeatSaverConnector beatSaverConnector,
                               SongService songService,
                               MapSuggestionRepository mapSuggestionRepository) {
        this.beatMapRepository = beatMapRepository;
        this.beatSaverConnector = beatSaverConnector;
        this.songService = songService;
        this.mapSuggestionRepository = mapSuggestionRepository;
    }

    public List<BeatMap> getAllSuggestedMaps() {
        return this.beatMapRepository.findAllSuggestedMaps();
    }

    public void addNewMapSuggestion(String beatSaverId, Long leaderBoardId, String difficulty, Double complexity, StaffUser staffUser) {
        BeatSaverSongInfo beatSaverSongInfo = this.beatSaverConnector.getMapInfoByKey(beatSaverId);

        if (this.beatMapRepository.existsById(leaderBoardId)) {
            throw new AccsaberOperationException(ExceptionType.RANKED_MAP_ALREADY_EXISTS, "The ranked map with the leaderboardId " + leaderBoardId + " already exists");
        }

        Song song = this.songService.getOrCreateSong(beatSaverSongInfo);

        BeatSaverDifficultyDetails beatSaverDifficultyDetails = beatSaverSongInfo.getMetadata().getCharacteristics().get(0).getDifficulties().get(difficulty);

        BeatMap suggestedMap = new BeatMap();
        suggestedMap.setLeaderboardId(leaderBoardId);
        suggestedMap.setMaxScore(this.calculateMaxScore(beatSaverDifficultyDetails.getNotes()));
        suggestedMap.setSong(song);
        suggestedMap.setComplexity(complexity);
        suggestedMap.setDifficulty(difficulty);
        suggestedMap.setRankedStage(RankedStage.SUGGESTED);
        song.getRankedMaps().add(suggestedMap);

        song = this.songService.saveSong(song);

        MapSuggestionVote mapSuggestionVote = new MapSuggestionVote();
        mapSuggestionVote.setBeatMap(suggestedMap);
        mapSuggestionVote.setStaffUser(staffUser);
        this.mapSuggestionRepository.save(mapSuggestionVote);

//        List<Score> nowRankedScores = this.scoreRepository.findAllByLeaderboardId(rankedMap.getLeaderboardId());
//        nowRankedScores.forEach(score -> {
//            score.setIsRankedMapScore(true);
//            score.setAccuracy(score.getScore() / (double) rankedMap.getMaxScore());
//            double ap = APUtils.calculateApByAcc(score.getAccuracy(), rankedMap.getComplexity());
//            score.setAp(ap);
//        });
//        this.scoreRepository.saveAll(nowRankedScores);
    }


    public void voteOnMap(long leaderboardId, int direction, StaffUser staffUser) {
        MapSuggestionVote suggestionByLeaderboard = this.mapSuggestionRepository.findSuggestionByLeaderboardIdAndStaffUser(leaderboardId, staffUser);
        suggestionByLeaderboard.setVote(direction);

        suggestionByLeaderboard = this.mapSuggestionRepository.save(suggestionByLeaderboard);
    }

    public void qualifyMap(long leaderboardId) {
        Optional<BeatMap> optionalBeatMap = this.beatMapRepository.findById(leaderboardId);

        if (optionalBeatMap.isEmpty()) {
            throw new AccsaberOperationException(ExceptionType.RANKED_MAP_NOT_FOUND, "Couldn't find BeatMap with the id: " + leaderboardId);
        }

        BeatMap beatMap = optionalBeatMap.get();
        beatMap.setRankedStage(RankedStage.QUALIFIED);
        beatMap = this.beatMapRepository.save(beatMap);
    }

    /**
     * There has to be a better way to do this
     * TODO move to utils/service class
     */
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
