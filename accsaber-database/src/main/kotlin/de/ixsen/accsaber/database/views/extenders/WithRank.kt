package de.ixsen.accsaber.database.views.extenders

import org.hibernate.annotations.Immutable
import javax.persistence.MappedSuperclass

@MappedSuperclass
@Immutable
abstract class WithRank {
    val ranking: Long? = null
    val ap: Double? = null
}