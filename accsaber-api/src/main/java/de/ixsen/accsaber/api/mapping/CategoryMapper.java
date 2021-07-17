package de.ixsen.accsaber.api.mapping;

import de.ixsen.accsaber.api.dtos.CategoryDto;
import de.ixsen.accsaber.database.model.Category;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryDto map(Category category);

    Category map(CategoryDto categoryDto);

    List<CategoryDto> map(List<Category> categories);
}
