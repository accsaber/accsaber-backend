package de.ixsen.accsaber.business

import de.ixsen.accsaber.database.model.staff.StaffUser
import de.ixsen.accsaber.database.repositories.model.staff.StaffUserRepository
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class AccSaberUserDetailsService(private val staffUserRepository: StaffUserRepository) : UserDetailsService {
    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(s: String): UserDetails {
        val user = staffUserRepository.findById(s)
        return user
            .map { staffUser: StaffUser? -> User(staffUser?.username, staffUser?.password, emptyList()) }
            .orElse(null)
    }
}