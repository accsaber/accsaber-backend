package de.ixsen.accsaber.api.mapping;

import de.ixsen.accsaber.api.dtos.CategoryDto;
import de.ixsen.accsaber.database.model.Category;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    @Mapping(target = "name", source = "categoryName")
    CategoryDto map(Category category);

    @InheritInverseConfiguration
    Category map(CategoryDto categoryDto);

    List<CategoryDto> map(List<Category> categories);
}
