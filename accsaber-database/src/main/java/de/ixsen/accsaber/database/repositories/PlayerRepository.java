package de.ixsen.accsaber.database.repositories;

import de.ixsen.accsaber.database.model.players.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlayerRepository extends JpaRepository<Player, String> {

    String RANKED_PLAYER_DB =
            "(select p.player_id as player_id, avatar_url, hmd, is_acc_champ, player_name, cp.ap, cp.average_acc, cp.average_ap_per_map, cp.ranked_plays from player p join " +
                    "(select " +
                    "sum(cp.ap)                        as ap, " +
                    "avg(cp.average_acc)               as average_acc, " +
                    "avg(cp.average_ap_per_map)        as average_ap_per_map, " +
                    "sum(cp.ranked_plays)              as ranked_plays, " +
                    "cp.player_id                      as player_id " +
                    "from category_performance cp " +
                    "group by cp.player_id) cp where p.player_id = cp.player_id)";

    String MAIN = "(select *, rank() over ( order by rpdb.ap desc) AS rank  from " + RANKED_PLAYER_DB + " rpdb)";

    @Query("SELECT DISTINCT player " +
            "FROM Player player " +
            "LEFT JOIN fetch player.scores ")
    List<Player> findAllWithScores();


    @Query(value = "SELECT * from " + MAIN + " rp where rp.player_id = ?1 ",
            nativeQuery = true)
    Optional<Player> findPlayerByPlayerIdWithrank(String playerId);

    @Query(value = "SELECT * from " + MAIN + " rp ", nativeQuery = true)
    List<Player> findAllWithrank();
}
