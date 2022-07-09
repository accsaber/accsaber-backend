package de.ixsen.accsaber.database.views

import org.hibernate.annotations.Immutable
import javax.persistence.Entity

@Entity
@Immutable
class CategoryAccSaberPlayer : AccSaberPlayer() {
    val categoryName: String? = null
}