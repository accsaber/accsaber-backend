package de.ixsen.accsaber.database.repositories;

import de.ixsen.accsaber.database.model.players.RankedPlayer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RankedPlayerRepository extends JpaRepository<RankedPlayer, String> {

    @Query(value = "SELECT * from ranked_player rp where rp.player_id = ?1 ",
            nativeQuery = true)
    Optional<RankedPlayer> findPlayerByPlayerId(String playerId);

    @Query(value = "SELECT * from ranked_player rp ", nativeQuery = true)
    List<RankedPlayer> findAllWithRanking();
}
