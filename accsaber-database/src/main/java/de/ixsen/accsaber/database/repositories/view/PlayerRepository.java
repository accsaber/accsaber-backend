package de.ixsen.accsaber.database.repositories.view;

import de.ixsen.accsaber.database.views.OverallPlayer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlayerRepository extends JpaRepository<OverallPlayer, String> {

    @Query(value = "SELECT * from ranked_player rp where rp.player_id = ?1 ",
            nativeQuery = true)
    Optional<OverallPlayer> findPlayerByPlayerId(String playerId);

    @Query(value = "SELECT * from ranked_player rp ", nativeQuery = true)
    List<OverallPlayer> findAllWithRanking();
}
