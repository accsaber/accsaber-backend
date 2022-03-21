package de.ixsen.accsaber.business;

import de.ixsen.accsaber.business.exceptions.AccsaberOperationException;
import de.ixsen.accsaber.business.exceptions.ExceptionType;
import de.ixsen.accsaber.database.model.maps.BeatMap;
import de.ixsen.accsaber.database.model.maps.Song;
import de.ixsen.accsaber.database.model.players.PlayerData;
import de.ixsen.accsaber.database.model.players.ScoreData;
import de.ixsen.accsaber.database.model.players.ScoreDataHistory;
import de.ixsen.accsaber.database.repositories.model.BeatMapRepository;
import de.ixsen.accsaber.database.repositories.model.ScoreDataHistoryRepository;
import de.ixsen.accsaber.database.repositories.model.ScoreDataRepository;
import de.ixsen.accsaber.database.repositories.view.AccSaberScoreRepository;
import de.ixsen.accsaber.database.views.AccSaberScore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ScoreService implements HasLogger {

    private final AccSaberScoreRepository accSaberScoreRepository;
    private final BeatMapRepository beatMapRepository;
    private final ScoreDataHistoryRepository scoreDataHistoryRepository;
    private final ScoreDataRepository scoreDataRepository;

    @Autowired
    public ScoreService(AccSaberScoreRepository accSaberScoreRepository,
                        ScoreDataHistoryRepository scoreDataHistoryRepository,
                        ScoreDataRepository scoreDataRepository,
                        BeatMapRepository beatMapRepository) {
        this.accSaberScoreRepository = accSaberScoreRepository;
        this.scoreDataHistoryRepository = scoreDataHistoryRepository;
        this.scoreDataRepository = scoreDataRepository;
        this.beatMapRepository = beatMapRepository;
    }

    public List<AccSaberScore> getScoresForLeaderboardId(Long leaderboardId) {
        return this.accSaberScoreRepository.findAllByLeaderboardId(leaderboardId);
    }

    @Transactional(readOnly = true)
    public List<AccSaberScore> getScoresForMapHash(String mapHash, String characteristic, String difficulty, int page, int pageSize) {
        Song song = new Song();
        song.setSongHash(mapHash);
        BeatMap beatMap = this.beatMapRepository.findBySongAndDifficulty(song, difficulty).orElseThrow(() -> new AccsaberOperationException(ExceptionType.RANKED_MAP_NOT_FOUND, "The map was not found."));
        return this.accSaberScoreRepository.findAllByLeaderboardId(beatMap.getLeaderboardId(), PageRequest.of(page, pageSize));

    }

    public List<AccSaberScore> getScoresForPlayer(PlayerData player) {
        return this.accSaberScoreRepository.findAllByPlayerOrderByApDesc(player);
    }

    public List<ScoreDataHistory> getScoreHistoryForPlayer(PlayerData player, long leaderboardId) {
        return this.scoreDataHistoryRepository.findAllByPlayerAndLeaderboardId(player, leaderboardId);
    }

    public Optional<ScoreData> getScoreByPlayerForLeaderboard(PlayerData player, long leaderboardId) {
        return this.scoreDataRepository.findByPlayerAndLeaderboardId(player, leaderboardId);
    }
}
