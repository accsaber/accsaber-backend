package de.ixsen.accsaber.business;

import de.ixsen.accsaber.database.model.maps.RankedMap;
import de.ixsen.accsaber.database.model.players.Player;
import de.ixsen.accsaber.database.model.players.RankedScore;
import de.ixsen.accsaber.database.model.players.Score;
import de.ixsen.accsaber.database.repositories.RankedMapRepository;
import de.ixsen.accsaber.database.repositories.RankedScoreRepository;
import de.ixsen.accsaber.database.repositories.ScoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.history.Revision;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ScoreService implements HasLogger {

    private final ScoreRepository scoreRepository;
    private final RankedScoreRepository rankedScoreRepository;
    private final RankedMapRepository rankedMapRepository;

    @Autowired
    public ScoreService(ScoreRepository scoreRepository, RankedScoreRepository rankedScoreRepository, RankedMapRepository rankedMapRepository) {
        this.scoreRepository = scoreRepository;
        this.rankedScoreRepository = rankedScoreRepository;
        this.rankedMapRepository = rankedMapRepository;
    }

    public List<RankedScore> getScoresForLeaderboardId(Long leaderboardId) {
        return this.rankedScoreRepository.findAllRankedMapsByLeaderboardId(leaderboardId);
    }

    public void recalculateApForAllScores() {
        List<Score> allRankedScores = this.scoreRepository.findAllRankedMaps();
        Map<Long, RankedMap> rankedMaps = new HashMap<>();

        for (RankedMap rankedMap : this.rankedMapRepository.findAll()) {
            rankedMaps.put(rankedMap.getLeaderboardId(), rankedMap);
        }

        allRankedScores.forEach(score -> {
            score.setAccuracy(score.getScore() / (double) rankedMaps.get(score.getLeaderboardId()).getMaxScore());
            double ap = APUtils.calculateApByAcc(score.getAccuracy(), rankedMaps.get(score.getLeaderboardId()).getTechyness());
            score.setAp(ap);
        });

        this.scoreRepository.saveAll(allRankedScores);
    }


    public List<RankedScore> getScoresForPlayer(Player player) {
        return this.rankedScoreRepository.findAllByPlayerOrderByApDesc(player);
    }

    public List<Score> getScoreHistoryForPlayer(Player player, long leaderboardId) {
        return this.scoreRepository
                .findByPlayerAndLeaderboardId(player, leaderboardId)
                .map(score -> this.scoreRepository.findRevisions(score.getScoreId())
                        .get()
                        .map(Revision::getEntity)
                        .collect(Collectors.toList()))
                .orElse(Collections.emptyList());
    }
}
