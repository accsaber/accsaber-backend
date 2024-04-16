package de.ixsen.accsaber.database.model.players

import de.ixsen.accsaber.database.model.maps.BeatMap
import org.hibernate.annotations.ColumnDefault
import java.time.Instant
import javax.persistence.*

@Entity
class ScoreData(
    @Id
    var scoreId: Long,
    var rankWhenScoresSet: Int,
    var score: Int,
    var unmodifiedScore: Int,
    var accuracy: Double? = null,
    var ap: Double? = null,
    var weightedAp: Double? = null,

    @OrderBy
    var timeSet: Instant,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(insertable = false, updatable = false, name = "map_leaderboard_id", foreignKey = ForeignKey(name = "none"))
    val beatMap: BeatMap,

    @Column(name = "map_leaderboard_id")
    var leaderboardId: Long,

    @ManyToOne
    @JoinColumn(name = "player_id")
    var player: PlayerData,

    @ColumnDefault("false")
    var isRankedScore: Boolean,
    var mods: String,
)
