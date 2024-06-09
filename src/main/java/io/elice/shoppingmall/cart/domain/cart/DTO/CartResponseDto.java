package io.elice.shoppingmall.cart.domain.cart.DTO;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CartResponseDto {
    private Long id;
    //private Long member_id;
    private LocalDateTime created_at;
}
