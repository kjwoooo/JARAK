package io.elice.shoppingmall.category.dto;

import io.elice.shoppingmall.category.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter @Setter
@NoArgsConstructor
public class CategoryDto {

    private String name;
    private Long parentId;

    public CategoryDto(String name, Long parentId) {
        this.name = name;
        this.parentId = parentId;
    }

    public Category toEntity() {
        Category category = new Category();
        category.setName(this.name);

        // parentId가 null이 아닌 경우에 부모 카테고리 설정
        if (this.parentId != null) {
            Category parentCategory = new Category();
            parentCategory.setId(this.parentId);
            category.setParent(parentCategory);
        }
        //null일 경우 기본값이 null 이기 때문에 null로 설정하지 않아도 됨
        return category;
    }


}
