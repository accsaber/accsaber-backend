package de.ixsen.accsaber.database.repositories;

import de.ixsen.accsaber.database.model.maps.RankedMap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RankedMapRepository extends JpaRepository<RankedMap, Long> {

    @Override
    @Query("FROM RankedMap map left join fetch map.song")
    List<RankedMap> findAll();
}
