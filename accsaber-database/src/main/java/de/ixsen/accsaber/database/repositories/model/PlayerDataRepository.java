package de.ixsen.accsaber.database.repositories.model;

import de.ixsen.accsaber.database.model.players.PlayerData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerDataRepository extends JpaRepository<PlayerData, String> {
}
