package de.ixsen.accsaber.business;

import de.ixsen.accsaber.database.model.maps.RankedMap;
import de.ixsen.accsaber.database.model.players.Score;
import de.ixsen.accsaber.database.repositories.RankedMapRepository;
import de.ixsen.accsaber.database.repositories.ScoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ScoreService implements HasLogger {

    private final ScoreRepository scoreRepository;
    private final RankedMapRepository rankedMapRepository;

    @Autowired
    public ScoreService(ScoreRepository scoreRepository, RankedMapRepository rankedMapRepository) {
        this.scoreRepository = scoreRepository;
        this.rankedMapRepository = rankedMapRepository;
    }

    public List<Score> getScoresForLeaderboardId(Long leaderboardId) {
        return this.scoreRepository.findAllByLeaderboardIdOrderByScoreDesc(leaderboardId);
    }

    public void recalculateApForAllScores() {
        List<Score> allRankedScores = this.scoreRepository.findAllByIsRankedMapScore(true);
        Map<Long, RankedMap> rankedMaps = new HashMap<>();

        for (RankedMap rankedMap : this.rankedMapRepository.findAll()) {
            rankedMaps.put(rankedMap.getLeaderboardId(), rankedMap);
        }

        allRankedScores.forEach(score -> {
            score.setIsRankedMapScore(true);
            score.setAccuracy(score.getScore() / (double) rankedMaps.get(score.getLeaderboardId()).getMaxScore());
            double ap = APUtils.calculateApByAcc(score.getAccuracy(), rankedMaps.get(score.getLeaderboardId()).getTechyness());
            score.setAp(ap);
        });

        this.scoreRepository.saveAll(allRankedScores);
    }


}
