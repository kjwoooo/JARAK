package io.elice.shoppingmall.cart.repository;

import io.elice.shoppingmall.cart.domain.cart.Entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {

}
