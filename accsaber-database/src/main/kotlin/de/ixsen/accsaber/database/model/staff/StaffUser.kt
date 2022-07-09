package de.ixsen.accsaber.database.model.staff

import javax.persistence.Entity
import javax.persistence.Id

@Entity
class StaffUser {
    @Id
    var username: String? = null
    var password: String? = null
    var role: String? = null
}