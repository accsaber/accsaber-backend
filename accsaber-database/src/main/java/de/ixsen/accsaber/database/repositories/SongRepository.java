package de.ixsen.accsaber.database.repositories;

import de.ixsen.accsaber.database.model.maps.Song;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SongRepository extends JpaRepository<Song, String> {
}
