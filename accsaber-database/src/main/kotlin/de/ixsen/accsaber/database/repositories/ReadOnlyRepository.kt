package de.ixsen.accsaber.database.repositories

import org.springframework.data.repository.NoRepositoryBean
import org.springframework.data.repository.Repository

@NoRepositoryBean
interface ReadOnlyRepository<T, ID, RT> : Repository<T, ID> {
    fun findAll(): List<RT>
}