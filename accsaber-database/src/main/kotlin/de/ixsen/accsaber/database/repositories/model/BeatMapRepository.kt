package de.ixsen.accsaber.database.repositories.model

import de.ixsen.accsaber.database.model.Category
import de.ixsen.accsaber.database.model.maps.BeatMap
import de.ixsen.accsaber.database.model.maps.Song
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface BeatMapRepository : JpaRepository<BeatMap, Long> {
    @Query("FROM BeatMap map left join fetch map.song")
    override fun findAll(): List<BeatMap>

    @Query("FROM BeatMap map left join fetch map.song where map.category.countsTowardsOverall = true")
    fun findAllCountingTowardsOverall(): List<BeatMap>

    @Query("FROM BeatMap map left join fetch map.song where map.category = ?1")
    fun findAllForCategory(category: Category): List<BeatMap>

    @Query("FROM BeatMap map where map.song = ?1 and map.difficulty = ?2")
    fun findBySongAndDifficulty(song: String, difficulty: String): Optional<BeatMap>
}