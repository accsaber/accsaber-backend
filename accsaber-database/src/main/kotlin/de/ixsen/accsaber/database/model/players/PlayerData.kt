package de.ixsen.accsaber.database.model.players

import de.ixsen.accsaber.database.model.PlayerCategoryStats
import org.hibernate.annotations.ColumnDefault
import org.springframework.data.annotation.CreatedDate
import java.time.Instant
import javax.persistence.*

@Entity
class PlayerData(
    @Id
    var playerId: Long,
    var playerName: String,
    var avatarUrl: String? = null,
    var hmd: String? = null,

    @OneToMany(mappedBy = "player")
    @OrderBy("timeSet desc")
    var scores: MutableList<ScoreData> = ArrayList(),

    @OneToMany(mappedBy = "player", cascade = [CascadeType.ALL])
    var playerCategoryStats: MutableSet<PlayerCategoryStats> = LinkedHashSet(),

    @ColumnDefault("false")
    var isAccChamp: Boolean = false,

    @CreatedDate
    @Column(nullable = false, updatable = false)
    var joinDate: Instant = Instant.now()
    ) {

    fun addScore(score: ScoreData) {
        scores.add(score)
        score.player = this
    }
}