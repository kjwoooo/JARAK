package io.elice.shoppingmall.cart.domain.cartItems.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartItemRequestDto {
    private Long id;
    private String size;
    private String color;
    private int quantity;
}
