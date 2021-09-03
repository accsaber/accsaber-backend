package de.ixsen.accsaber.business;

import de.ixsen.accsaber.database.model.players.PlayerData;
import de.ixsen.accsaber.database.model.players.ScoreData;
import de.ixsen.accsaber.database.model.players.ScoreDataHistory;
import de.ixsen.accsaber.database.repositories.model.ScoreDataHistoryRepository;
import de.ixsen.accsaber.database.repositories.model.ScoreDataRepository;
import de.ixsen.accsaber.database.repositories.view.AccSaberScoreRepository;
import de.ixsen.accsaber.database.views.AccSaberScore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ScoreService implements HasLogger {

    private final AccSaberScoreRepository accSaberScoreRepository;
    private final ScoreDataHistoryRepository scoreDataHistoryRepository;
    private final ScoreDataRepository scoreDataRepository;

    @Autowired
    public ScoreService(AccSaberScoreRepository accSaberScoreRepository,
                        ScoreDataHistoryRepository scoreDataHistoryRepository,
                        ScoreDataRepository scoreDataRepository) {
        this.accSaberScoreRepository = accSaberScoreRepository;
        this.scoreDataHistoryRepository = scoreDataHistoryRepository;
        this.scoreDataRepository = scoreDataRepository;
    }

    public List<AccSaberScore> getScoresForLeaderboardId(Long leaderboardId) {
        return this.accSaberScoreRepository.findAllByLeaderboardId(leaderboardId);
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
