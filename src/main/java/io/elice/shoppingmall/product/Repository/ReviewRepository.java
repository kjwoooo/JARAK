package io.elice.shoppingmall.product.Repository;

import io.elice.shoppingmall.product.Entity.Item.Item;
import io.elice.shoppingmall.product.Entity.Review.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    //FIx
//    List<Review> findAllByItemOrderByIdAsc(Item item);
//    Page<Review> findAllByItem_IdOrderByCreatedAtDesc(Long itemId, Pageable pageable);
    List<Review> findAllByItem_IdOrderByCreatedAtDesc(Long itemId);

}
