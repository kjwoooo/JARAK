package io.elice.shoppingmall.category.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 상의 - 반팔티, 긴팔티, 후드티 <p>
 * 하의 - 긴바지, 반바지 등 <p>
 * 메인 카테고리의 하위 카테고리에 관한 entity
 * */
@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class SubCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;
}
