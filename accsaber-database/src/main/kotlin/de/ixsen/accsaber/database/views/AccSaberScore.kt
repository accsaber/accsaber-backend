package de.ixsen.accsaber.database.views

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
    val unmodifiedScore = 0
    val accuracy: Double? = null

    @OrderBy
    val timeSet: Instant? = null

    val leaderboardId: Long? = null

    @ManyToOne
    @JoinColumn(name = "player_id")
    val player: PlayerData? = null

    val mods: String? = null

    var songName: String? = null
    var songAuthorName: String? = null
    var levelAuthorName: String? = null
    var complexity = 0.0
    var songHash: String? = null
    var difficulty: String? = null
    var beatSaverKey: String? = null
    var categoryDisplayName: String? = null
    var categoryName: String? = null
}
