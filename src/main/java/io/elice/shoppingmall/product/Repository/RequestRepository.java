package io.elice.shoppingmall.product.Repository;

import io.elice.shoppingmall.product.Entity.Item.Item;
import io.elice.shoppingmall.product.Entity.Review.Request;
import io.elice.shoppingmall.product.Entity.Review.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Integer> {
    List<Request> findAllByItemOrderByIdAsc(Item item);
}
