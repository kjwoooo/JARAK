package io.elice.shoppingmall.cart.repository;

import io.elice.shoppingmall.cart.domain.cart.Entity.Cart;
import io.elice.shoppingmall.cart.domain.cartItems.DTO.CartItemResponseDto;
import io.elice.shoppingmall.cart.domain.cartItems.Entity.CartItems;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CartItemRepository extends JpaRepository<CartItems, Long> {
    @Query("SELECT ci FROM CartItems ci JOIN FETCH ci.item_id WHERE ci.cart_id = :cartId")
    List<CartItems> findByCartId(@Param("cartId") Long cartId);
}
