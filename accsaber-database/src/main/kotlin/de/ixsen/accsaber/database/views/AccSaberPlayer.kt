package de.ixsen.accsaber.database.views

import de.ixsen.accsaber.database.views.extenders.WithRankAndStats
import org.hibernate.annotations.Immutable
import javax.persistence.Id
import javax.persistence.MappedSuperclass

@MappedSuperclass
@Immutable
abstract class AccSaberPlayer : WithRankAndStats() {
    @Id
    val playerId: Long? = null
    val playerName: String? = null
    val avatarUrl: String? = null
    val isAccChamp: Boolean? = null
    val hmd: String? = null
    val rankingLastWeek: Long? = null
}