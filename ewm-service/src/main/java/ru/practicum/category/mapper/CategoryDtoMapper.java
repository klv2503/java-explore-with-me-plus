package ru.practicum.category.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.model.Category;

import java.util.List;

@Component
public class CategoryDtoMapper {

    public CategoryDto mapCategoryToDto(Category cat) {
        return new CategoryDto(cat.getId(), cat.getName());
    }

    public Category mapDtoToCategory(CategoryDto catDto) {
        return new Category(catDto.getId(), catDto.getName());
    }

    public List<CategoryDto> mapCatListToDtoList(List<Category> cats) {
        return cats.stream()
                .map(this::mapCategoryToDto)
                .toList();
    }
}
