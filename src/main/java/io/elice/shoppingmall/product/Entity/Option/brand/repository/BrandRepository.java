package io.elice.shoppingmall.product.Entity.Option.brand.repository;

import io.elice.shoppingmall.product.Entity.Option.brand.entity.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Long> {
    boolean existsByName(String name);
}
