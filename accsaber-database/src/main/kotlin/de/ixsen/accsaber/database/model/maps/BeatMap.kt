package de.ixsen.accsaber.database.model.maps

import de.ixsen.accsaber.database.model.Category
import org.hibernate.engine.internal.Cascade
import org.springframework.data.annotation.CreatedDate
import java.time.Instant
import javax.persistence.*

@Entity
class BeatMap(
    /**
     * Scoresaber LeaderboardID
     */
    @Id
    val leaderboardId: Long,
    val maxScore: Int,

    @ManyToOne
    @JoinColumn(name = "song")
    val song: Song,
    val difficulty: String,
    var complexity: Double,

    @ManyToOne
    @JoinColumn(name = "category_id")
    val category: Category,

    @OrderBy
    @CreatedDate
    @Column(nullable = false, updatable = false)
    val dateRanked: Instant = Instant.now()
)
