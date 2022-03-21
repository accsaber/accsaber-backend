package de.ixsen.accsaber.business.staff

import de.ixsen.accsaber.database.model.staff.StaffUser
import de.ixsen.accsaber.database.repositories.model.StaffUserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class StaffService @Autowired constructor(private val bCryptPasswordEncoder: PasswordEncoder, private val staffUserRepository: StaffUserRepository) {
    fun saveNewUser(username: String?, password: String?): StaffUser {
        val staffUser = StaffUser()
        staffUser.username = username
        staffUser.password = bCryptPasswordEncoder.encode(password)
        staffUserRepository.save(staffUser)
        return staffUser
    }
}