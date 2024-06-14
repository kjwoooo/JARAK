package io.elice.shoppingmall.cart.repository;

import io.elice.shoppingmall.cart.domain.cartItems.Entity.CartItems;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CartItemRepository extends JpaRepository<CartItems, Long> {
    @Query("SELECT ci.id, ci.size, ci.color, ci.quantity, i.id, i.itemName, i.price " +
        "FROM CartItems ci " +
        "JOIN ci.item_id i " +
        "WHERE ci.cart_id.id = :cartId")
    List<Object[]> findAllItemsByCartId(@Param("cartId") Long cartId);
}
