package de.ixsen.accsaber.database.repositories.model

import de.ixsen.accsaber.database.model.maps.Song
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface SongRepository : JpaRepository<Song, String>