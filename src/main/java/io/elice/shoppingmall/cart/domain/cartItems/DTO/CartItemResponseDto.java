package io.elice.shoppingmall.cart.domain.cartItems.DTO;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CartItemResponseDto {
    private Long id;
    private int quantity;
    private LocalDateTime created_at;
}
