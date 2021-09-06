package de.ixsen.accsaber.database.repositories.model;

import de.ixsen.accsaber.database.model.Category;
import de.ixsen.accsaber.database.model.maps.BeatMap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BeatMapRepository extends JpaRepository<BeatMap, Long> {

    @Override
    @Query("FROM BeatMap map left join fetch map.song")
    List<BeatMap> findAll();

    @Query("FROM BeatMap map left join fetch map.song where map.category.countsTowardsOverall = true")
    List<BeatMap> findAllCountingTowardsOverall();

    @Query("FROM BeatMap map left join fetch map.song where map.category = ?1")
    List<BeatMap> findAllForCategory(Category category);
}
