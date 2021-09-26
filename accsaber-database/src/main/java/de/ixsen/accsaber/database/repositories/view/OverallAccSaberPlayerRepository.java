package de.ixsen.accsaber.database.repositories.view;

import de.ixsen.accsaber.database.repositories.ReadOnlyRepository;
import de.ixsen.accsaber.database.views.AccSaberPlayer;
import de.ixsen.accsaber.database.views.OverallAccSaberPlayer;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OverallAccSaberPlayerRepository extends ReadOnlyRepository<OverallAccSaberPlayer, Long, AccSaberPlayer> {

    Optional<AccSaberPlayer> findPlayerByPlayerId(Long playerId);

}
