package de.ixsen.accsaber.database.repositories.model;

import de.ixsen.accsaber.database.model.players.PlayerData;
import de.ixsen.accsaber.database.model.players.ScoreDataHistory;
import de.ixsen.accsaber.database.repositories.ReadOnlyRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScoreDataHistoryRepository extends ReadOnlyRepository<ScoreDataHistory, Long, ScoreDataHistory> {

    List<ScoreDataHistory> findAllByPlayerAndLeaderboardId(PlayerData playerData, Long scoreId);
}
