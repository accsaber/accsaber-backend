package de.ixsen.accsaber.business;

import de.ixsen.accsaber.database.model.maps.BeatMap;
import de.ixsen.accsaber.database.model.players.PlayerData;
import de.ixsen.accsaber.database.views.AccSaberScore;
import de.ixsen.accsaber.database.model.players.ScoreData;
import de.ixsen.accsaber.database.repositories.model.RankedMapRepository;
import de.ixsen.accsaber.database.repositories.view.AccSaberScoreRepository;
import de.ixsen.accsaber.database.repositories.model.ScoreDataRepository;
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

    private final ScoreDataRepository scoreDataRepository;
    private final AccSaberScoreRepository accSaberScoreRepository;
    private final RankedMapRepository rankedMapRepository;

    @Autowired
    public ScoreService(ScoreDataRepository scoreDataRepository, AccSaberScoreRepository accSaberScoreRepository, RankedMapRepository rankedMapRepository) {
        this.scoreDataRepository = scoreDataRepository;
        this.accSaberScoreRepository = accSaberScoreRepository;
        this.rankedMapRepository = rankedMapRepository;
    }

    public List<AccSaberScore> getScoresForLeaderboardId(Long leaderboardId) {
        return this.accSaberScoreRepository.findAllRankedMapsByLeaderboardId(leaderboardId);
    }

    public void recalculateApForAllScores() {
        List<ScoreData> allRankedScores = this.scoreDataRepository.findAllRankedMaps();
        Map<Long, BeatMap> rankedMaps = new HashMap<>();

        for (BeatMap beatMap : this.rankedMapRepository.findAll()) {
            rankedMaps.put(beatMap.getLeaderboardId(), beatMap);
        }

        allRankedScores.forEach(score -> {
            score.setAccuracy(score.getScore() / (double) rankedMaps.get(score.getLeaderboardId()).getMaxScore());
            double ap = APUtils.calculateApByAcc(score.getAccuracy(), rankedMaps.get(score.getLeaderboardId()).getComplexity());
            score.setAp(ap);
        });

        this.scoreDataRepository.saveAll(allRankedScores);
    }


    public List<AccSaberScore> getScoresForPlayer(PlayerData player) {
        return this.accSaberScoreRepository.findAllByPlayerOrderByApDesc(player);
    }

    public List<ScoreData> getScoreHistoryForPlayer(PlayerData player, long leaderboardId) {
        return this.scoreDataRepository
                .findByPlayerAndLeaderboardId(player, leaderboardId)
                .map(score -> this.scoreDataRepository.findRevisions(score.getScoreId())
                        .get()
                        .map(Revision::getEntity)
                        .collect(Collectors.toList()))
                .orElse(Collections.emptyList());
    }
}
