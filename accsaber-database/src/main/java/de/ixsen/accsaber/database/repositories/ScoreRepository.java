package de.ixsen.accsaber.database.repositories;

import de.ixsen.accsaber.database.model.players.Score;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScoreRepository extends JpaRepository<Score, Long> {

    List<Score> findAllByLeaderboardId(Long leaderboardId);

    @Query(value = "select * from score s where s.is_ranked_map_score = true", nativeQuery = true)
    List<Score> findAllRankedMaps();
}
