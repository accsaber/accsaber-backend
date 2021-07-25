package de.ixsen.accsaber.database.repositories.view;

import de.ixsen.accsaber.database.model.players.PlayerData;
import de.ixsen.accsaber.database.repositories.ReadOnlyRepository;
import de.ixsen.accsaber.database.views.AccSaberScore;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccSaberScoreRepository extends ReadOnlyRepository<AccSaberScore, Long, AccSaberScore> {

    List<AccSaberScore> findAllByLeaderboardId(Long leaderboardId);

    List<AccSaberScore> findAllByPlayerOrderByApDesc(PlayerData playerData);
}
