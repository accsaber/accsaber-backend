package de.ixsen.accsaber.database.model

import de.ixsen.accsaber.database.model.players.PlayerData
import org.hibernate.annotations.ColumnDefault
import javax.persistence.*

@Entity
@IdClass(PlayerCategoryStatsPK::class)
class PlayerCategoryStats(
    @ManyToOne
    @JoinColumn(name = "player_id")
    @Id
    val player: PlayerData,

    @ManyToOne
    @JoinColumn(name = "category_id")
    @Id
    val category: Category,

    @ColumnDefault("0")
    var averageAcc: Double = 0.0,

    @ColumnDefault("0")
    var ap: Double = 0.0,

    @ColumnDefault("0")
    var averageAp: Double = 0.0,

    @ColumnDefault("0")
    var rankedPlays: Int = 0,

    @ColumnDefault("0")
    var rankingLastWeek: Int = 0
)