package io.elice.shoppingmall.category.dto;

import io.elice.shoppingmall.category.entity.Category;
import io.elice.shoppingmall.category.entity.SubCategory;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class SubCategoryDto {

    private Long id;
    private String name;
    private Category category;

    public SubCategoryDto(SubCategory subCategory) {
        this.id = subCategory.getId();
        this.name = subCategory.getName();
        this.category = subCategory.getCategory();
    }

    public SubCategory toEntity() {
        return new SubCategory(id, name,category);
    }


}

