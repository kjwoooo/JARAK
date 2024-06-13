package io.elice.shoppingmall.cart.domain.cartItems.DTO;

import io.elice.shoppingmall.cart.domain.cart.Entity.Cart;
import io.elice.shoppingmall.cart.domain.cartItems.Entity.CartItems;
import io.elice.shoppingmall.product.Entity.Item.Item;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartItemResponseDto {
    private Long id;
    private int quantity;
    private String item_name;
    private int price;
    private Long item_id;
    private String size;
    private String color;

    public CartItems toEntity(Cart cart, Item item) {
        CartItems cartItem = new CartItems();
        cartItem.setId(this.id);
        cartItem.setSize(this.size);
        cartItem.setColor(this.color);
        cartItem.setQuantity(this.quantity);
        cartItem.setItem_id(item);
        cartItem.setCart_id(cart);
        return cartItem;
    }
}
