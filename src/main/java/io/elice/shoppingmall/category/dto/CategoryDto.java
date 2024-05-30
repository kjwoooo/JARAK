package io.elice.shoppingmall.category.dto;

import io.elice.shoppingmall.category.entity.Category;

import java.util.List;
import java.util.stream.Collectors;

public class CategoryDto {

    private Long id;
    private String name;
    private List<SubCategoryDto> subCategories;

    public CategoryDto(Category category) {
        this.id = category.getId();
        this.name = category.getName();
        this.subCategories = category.getSubCategories().stream()
                .map(SubCategoryDto::new)
                .collect(Collectors.toList());
    }

}
