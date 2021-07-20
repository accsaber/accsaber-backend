package de.ixsen.accsaber.database.repositories.view;

import de.ixsen.accsaber.database.views.PlayerOverallStats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerOverallStatsRepository extends JpaRepository<PlayerOverallStats, Long> {
}
