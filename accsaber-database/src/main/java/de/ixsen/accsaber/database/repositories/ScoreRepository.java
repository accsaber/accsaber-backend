package de.ixsen.accsaber.database.repositories;

import de.ixsen.accsaber.database.model.players.Score;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScoreRepository extends JpaRepository<Score, Long> {

    List<Score> findAllByLeaderboardId(Long leaderboardId);

    List<Score> findAllByLeaderboardIdOrderByScoreDesc(Long leaderboardId);

    List<Score> findAllByIsRankedMapScore(boolean ranked);

}
