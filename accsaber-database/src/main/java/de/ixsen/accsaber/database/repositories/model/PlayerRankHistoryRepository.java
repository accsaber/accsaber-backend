package de.ixsen.accsaber.database.repositories.model;

import de.ixsen.accsaber.database.model.players.PlayerRankHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlayerRankHistoryRepository extends JpaRepository<PlayerRankHistory, Long> {

    @Query(value = "select category_id, date, player_id, sum(ap) as ap, avg(average_acc) as average_acc, avg(average_ap_per_map) as average_ap_per_map, sum(ranked_plays) as ranked_plays, avg(p.ranking) as ranking " +
            "from player_rank_history p " +
            "where p.date > current_date - 30 and p.category_id = -1 and p.player_id = ?1 " +
            "group by p.date",
            nativeQuery = true)
    List<PlayerRankHistory> findLastMonthForPlayer(Long playerId);

    @Query(value = "select category_id, date, player_id, sum(ap) as ap, avg(average_acc) as average_acc, avg(average_ap_per_map) as average_ap_per_map, sum(ranked_plays) as ranked_plays, avg(p.ranking) as ranking " +
            "from player_rank_history p " +
            "where p.date > current_date - 30 and p.player_id = ?1 and p.category_id = ?2 " +
            "group by p.date",
            nativeQuery = true)
    List<PlayerRankHistory> findLastMonthForPlayerAndCategory(Long playerId, Long categoryId);
}
