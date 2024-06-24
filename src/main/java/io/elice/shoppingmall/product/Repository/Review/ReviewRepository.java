package io.elice.shoppingmall.product.Repository.Review;


import io.elice.shoppingmall.product.Entity.Review.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findAllByItem_IdOrderByCreatedAtDesc(Long itemId);

    List<Review> findAllByItemIdOrderByCreatedAtDesc(Long itemId);

    List<Review> findAllByUsername(String username);

    @Query("SELECT AVG(r.rate) FROM Review r WHERE r.item.id = :itemId")
    Double findAverageRateByItemId(@Param("itemId") Long itemId);
}
