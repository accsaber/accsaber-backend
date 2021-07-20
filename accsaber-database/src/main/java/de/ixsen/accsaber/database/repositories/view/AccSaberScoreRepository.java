package de.ixsen.accsaber.database.repositories.view;

import de.ixsen.accsaber.database.model.players.PlayerData;
import de.ixsen.accsaber.database.views.AccSaberScore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccSaberScoreRepository extends JpaRepository<AccSaberScore, Long> {

    @Query(value = "SELECT * FROM ranked_score rs where rs.map_leaderboard_id = ?1 order by rs.ranking", nativeQuery = true)
    List<AccSaberScore> findAllRankedMapsByLeaderboardId(Long leaderboardId);

    @Query(value = "SELECT * FROM ranked_score", nativeQuery = true)
    List<AccSaberScore> findAllByIsRankedMapScore();

    List<AccSaberScore> findAllByPlayerOrderByApDesc(PlayerData playerId);
}
