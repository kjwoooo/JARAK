package io.elice.shoppingmall.category.repository;

import io.elice.shoppingmall.category.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    boolean existsByName(String name);

    boolean existsByNameAndParentId(String name, Long parentId);
}
