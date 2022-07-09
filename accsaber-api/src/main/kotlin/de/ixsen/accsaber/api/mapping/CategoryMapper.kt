package de.ixsen.accsaber.api.mapping

import de.ixsen.accsaber.api.dtos.CategoryDto
import de.ixsen.accsaber.database.model.Category
import org.mapstruct.InheritInverseConfiguration
import org.mapstruct.Mapper
import org.mapstruct.Mapping

@Mapper(componentModel = "spring")
interface CategoryMapper {
    @Mapping(target = "categoryName", source = "categoryName")
    fun map(category: Category): CategoryDto

    @InheritInverseConfiguration
    fun map(categoryDto: CategoryDto): Category
    fun map(categories: List<Category>): List<CategoryDto>
}