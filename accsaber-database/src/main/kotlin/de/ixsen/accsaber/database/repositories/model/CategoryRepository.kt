package de.ixsen.accsaber.database.repositories.model

import de.ixsen.accsaber.database.model.Category
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface CategoryRepository : JpaRepository<Category, String> {
    fun existsByCategoryName(categoryName: String): Boolean
    fun findByCategoryName(categoryName: String): Optional<Category>
    fun deleteByCategoryName(categoryName: String)
}