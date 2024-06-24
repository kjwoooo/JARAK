package io.elice.shoppingmall.category.entity;

import io.elice.shoppingmall.category.dto.CategoryDto;
import io.elice.shoppingmall.entity.baseEntity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 상위 카테고리에 대한 하위 카테고리를<p>
 * 계층형 구조로 나타냄
 * */
@Entity
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class Category extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Category parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Category> subCategories = new ArrayList<>();

    public CategoryDto entityToDto() {
        return new CategoryDto(this.id, this.name,
                Optional.ofNullable(this.parent)
                        .map(Category::getId)
                        .orElse(null));
    }

}
