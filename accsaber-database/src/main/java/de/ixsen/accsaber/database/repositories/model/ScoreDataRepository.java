package de.ixsen.accsaber.database.repositories.model;

import de.ixsen.accsaber.database.model.players.PlayerData;
import de.ixsen.accsaber.database.model.players.ScoreData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.history.RevisionRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ScoreDataRepository extends JpaRepository<ScoreData, Long>, RevisionRepository<ScoreData, Long, Integer> {

    String RANKED_SCORES = "(select *, " +
            "rank() over ( partition by s.map_leaderboard_id order by s.score desc) AS ranking " +
            "from score s " +
            "where s.map_leaderboard_id IS NOT NULL)";

    List<ScoreData> findAllByLeaderboardId(Long leaderboardId);

    @Query(value = "select * from score s where s.map_leaderboard_id IS NOT NULL", nativeQuery = true)
    List<ScoreData> findAllRankedMaps();

    Optional<ScoreData> findByPlayerAndLeaderboardId(PlayerData player, long leaderboardId);


    @Query(value = "SELECT * FROM " + RANKED_SCORES + " rs where rs.map_leaderboard_id = ?1 order by rs.rank", nativeQuery = true)
    List<ScoreData> findAllRankedMapsByLeaderboardId(Long leaderboardId);

    @Query(value = "SELECT * FROM " + RANKED_SCORES, nativeQuery = true)
    List<ScoreData> findAllByIsRankedMapScore();

    List<ScoreData> findAllByPlayerOrderByApDesc(PlayerData playerId);

}
