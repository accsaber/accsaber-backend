package de.ixsen.accsaber.database.repositories;

import de.ixsen.accsaber.database.model.players.Player;
import de.ixsen.accsaber.database.model.players.Score;
import org.springframework.data.history.Revisions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.history.RevisionRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ScoreRepository extends JpaRepository<Score, Long>, RevisionRepository<Score, Long, Integer> {

    List<Score> findAllByLeaderboardId(Long leaderboardId);

    @Query(value = "select * from score s where s.map_leaderboard_id IS NOT NULL", nativeQuery = true)
    List<Score> findAllRankedMaps();

    Optional<Score> findByPlayerAndLeaderboardId(Player player, long leaderboardId);
}
