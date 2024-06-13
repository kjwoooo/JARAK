package io.elice.shoppingmall.cart.repository;

import io.elice.shoppingmall.cart.domain.cart.Entity.Cart;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CartRepository extends JpaRepository<Cart, Long> {


}
