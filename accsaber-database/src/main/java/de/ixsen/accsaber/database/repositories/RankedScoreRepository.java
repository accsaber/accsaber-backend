package de.ixsen.accsaber.database.repositories;

import de.ixsen.accsaber.database.model.players.Player;
import de.ixsen.accsaber.database.model.players.RankedScore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RankedScoreRepository extends JpaRepository<RankedScore, Long> {

    @Query(value = "SELECT * FROM ranked_score rs where rs.map_leaderboard_id = ?1 order by rs.ranking", nativeQuery = true)
    List<RankedScore> findAllRankedMapsByLeaderboardId(Long leaderboardId);

    @Query(value = "SELECT * FROM ranked_score", nativeQuery = true)
    List<RankedScore> findAllByIsRankedMapScore();

    List<RankedScore> findAllByPlayerOrderByApDesc(Player playerId);
}
