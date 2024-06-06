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
    private Long categoryId;
    public SubCategoryDto(SubCategory subCategory) {
        this.id = subCategory.getId();
        this.name = subCategory.getName();
        this.categoryId = subCategory.getCategory().getId();
    }
    public SubCategory toEntity() {
        SubCategory subCategory = new SubCategory();
        subCategory.setId(this.id);
        subCategory.setName(this.name);
        //  해당하는 상위 카테고리에 대한 정보를 찾아서 설정
        return subCategory;
    }


}

