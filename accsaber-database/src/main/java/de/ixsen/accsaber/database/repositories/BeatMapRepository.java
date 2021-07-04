package de.ixsen.accsaber.database.repositories;

import de.ixsen.accsaber.database.model.maps.BeatMap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BeatMapRepository extends JpaRepository<BeatMap, Long> {

    @Query("FROM BeatMap map left join fetch map.song where map.rankedStage = 'SUGGESTED'")
    List<BeatMap> findAllSuggestedMaps();

    @Query("FROM BeatMap map left join fetch map.song where map.rankedStage = 'QUALIFIED'")
    List<BeatMap> findAllQualifiedMaps();

    @Query("FROM BeatMap map left join fetch map.song where map.rankedStage = 'RANKED'")
    List<BeatMap> findAllRankedMaps();
}
