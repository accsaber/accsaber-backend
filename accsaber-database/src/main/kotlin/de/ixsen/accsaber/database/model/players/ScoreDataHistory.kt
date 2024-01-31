package de.ixsen.accsaber.database.model.players

import de.ixsen.accsaber.database.model.BaseEntity
import de.ixsen.accsaber.database.model.maps.BeatMap
import org.hibernate.annotations.Immutable
import java.time.Instant
import javax.persistence.*

@Entity
@Immutable
class ScoreDataHistory(
    val scoreId: Long,
    val score: Int,
    val unmodifiedScore: Int,
    val accuracy: Double,
    val ap: Double,
    val weightedAp: Double,

    @OrderBy
    val timeSet: Instant,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(insertable = false, updatable = false, name = "map_leaderboard_id", foreignKey = ForeignKey(name = "none"))
    val beatMap: BeatMap,

    @Column(name = "map_leaderboard_id")
    val leaderboardId: Long,

    @ManyToOne
    @JoinColumn(name = "player_id")
    val player: PlayerData,
    val mods: String,
) : BaseEntity()
