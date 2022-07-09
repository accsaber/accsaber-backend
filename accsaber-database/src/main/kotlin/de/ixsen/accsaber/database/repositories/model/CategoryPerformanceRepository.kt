package de.ixsen.accsaber.database.repositories.model

import de.ixsen.accsaber.database.model.PlayerCategoryStats
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CategoryPerformanceRepository : JpaRepository<PlayerCategoryStats, Long>