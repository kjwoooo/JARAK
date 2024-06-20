package io.elice.shoppingmall.product.Repository;

import io.elice.shoppingmall.product.Entity.Review.ReviewImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewImageRepository extends JpaRepository<ReviewImage, Long> {
}
