package io.elice.shoppingmall.product.Repository;

import io.elice.shoppingmall.member.entity.Member;
import io.elice.shoppingmall.product.Entity.Item.Item;
import io.elice.shoppingmall.product.Entity.Review.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findAllByItem_IdOrderByCreatedAtDesc(Long itemId);

    List<Review> findAllByItemIdOrderByCreatedAtDesc(Long itemId);

    List<Review> findAllByUsername(String username);

    @Query("SELECT AVG(r.rate) FROM Review r WHERE r.item.id = :itemId")
    Double findAverageRateByItemId(@Param("itemId") Long itemId);
}
