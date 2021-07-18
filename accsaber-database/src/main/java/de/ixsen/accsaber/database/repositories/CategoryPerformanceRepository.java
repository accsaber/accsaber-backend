package de.ixsen.accsaber.database.repositories;

import de.ixsen.accsaber.database.model.Category;
import de.ixsen.accsaber.database.model.CategoryPerformance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryPerformanceRepository extends JpaRepository<CategoryPerformance, Long> {
    String CATEGORY_PERFORMANCE_WITH_rank = "select *, rank() over ( order by cp.ap desc) AS rank from category_performance cp";

    String OVERALL_CATEGORY_PERFORMANCE_WITH_rank = "(SELECT " +
            "sum(cp.ap)                            AS ap, " +
            "avg(cp.average_acc)                   AS average_acc, " +
            "avg(cp.average_ap_per_map)            AS average_ap_per_map, " +
            "sum(cp.ranked_plays)                  AS ranked_plays, " +
            "cp.player_id                   AS player_player_id, " +
            "rank() over ( order by ap desc)       AS rank " +
            "FROM category_performance cp GROUP BY cp.player_id)";

    @Query(value = "SELECT * from " + CATEGORY_PERFORMANCE_WITH_rank + " rp where rp.player_id = ?1 and category_name = ?2",
            nativeQuery = true)
    Optional<CategoryPerformance> findCategoryPerformanceByPlayerIdWithrank(String playerId, Category category);

    @Query(value = "SELECT * from " + CATEGORY_PERFORMANCE_WITH_rank + " rp where category_name = ?2", nativeQuery = true)
    List<CategoryPerformance> findAllWithrank(Category category);

    @Query(value = "SELECT * from " + CategoryPerformanceRepository.OVERALL_CATEGORY_PERFORMANCE_WITH_rank + " rp ", nativeQuery = true)
    List<CategoryPerformance> findOverallrank();

    @Query(value = "SELECT * from " + CategoryPerformanceRepository.OVERALL_CATEGORY_PERFORMANCE_WITH_rank + " rp  where rp.player_id = ?1 ", nativeQuery = true)
    List<CategoryPerformance> findOverallrankWithPlayerId(String playerId);
}
