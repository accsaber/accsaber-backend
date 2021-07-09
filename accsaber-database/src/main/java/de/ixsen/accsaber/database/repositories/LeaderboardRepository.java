package de.ixsen.accsaber.database.repositories;

import de.ixsen.accsaber.database.model.Leaderboard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LeaderboardRepository extends JpaRepository<Leaderboard, String> {
}
