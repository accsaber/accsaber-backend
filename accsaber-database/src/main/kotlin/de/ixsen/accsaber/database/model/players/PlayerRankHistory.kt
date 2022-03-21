package de.ixsen.accsaber.database.model.players

import de.ixsen.accsaber.database.model.Category
import java.time.LocalDate
import javax.persistence.*

@Entity
@IdClass(PlayerRankHistoryPK::class)
class PlayerRankHistory(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(insertable = false, updatable = false, name = "player_id", foreignKey = ForeignKey(name = "none"))
    @Id
    var player: PlayerData,

    @Id
    var date: LocalDate,
    var ranking: Int,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(insertable = false, updatable = false, name = "category_id", foreignKey = ForeignKey(name = "none"))
    @Id
    var category: Category,
    var ap: Double,
    var averageAcc: Double,
    var averageApPerMap: Double,
    var rankedPlays: Int,
)