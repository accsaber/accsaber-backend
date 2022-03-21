package de.ixsen.accsaber.database.views.extenders

import org.hibernate.annotations.Immutable
import javax.persistence.MappedSuperclass

@MappedSuperclass
@Immutable
abstract class WithRankAndStats : WithRank() {
    val averageAcc: Double? = null
    val averageApPerMap: Double? = null
    val rankedPlays = 0
}