package de.ixsen.accsaber.database.repositories;

import de.ixsen.accsaber.database.model.players.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlayerRepository extends JpaRepository<Player, String> {

    @Query("SELECT DISTINCT player " +
            "FROM Player player " +
            "LEFT JOIN fetch player.scores ")
    List<Player> findAllWithScores();
}
