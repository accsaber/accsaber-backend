package de.ixsen.accsaber.database.views

import de.ixsen.accsaber.database.model.maps.BeatMap
import de.ixsen.accsaber.database.model.players.PlayerData
import de.ixsen.accsaber.database.views.extenders.WithRankAndWeightedAp
import org.hibernate.annotations.Immutable
import java.time.Instant
import javax.persistence.*

@Entity
@Immutable
class AccSaberScore : WithRankAndWeightedAp() {
    @Id
    val scoreId: Long? = null
    val rankWhenScoresSet = 0
    val score = 0
    val unmodififiedScore = 0
    val accuracy: Double? = null

    @OrderBy
    val timeSet: Instant? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(insertable = false, updatable = false, name = "map_leaderboard_id")
    val beatMap: BeatMap? = null

    @Column(name = "map_leaderboard_id")
    val leaderboardId: Long? = null

    @ManyToOne
    @JoinColumn(name = "player_id")
    val player: PlayerData? = null
}