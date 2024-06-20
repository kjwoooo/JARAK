package io.elice.shoppingmall.cart.domain.cartItems.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
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
    @NotBlank(message = "사이즈를 선택해주세요.")
    private String size;

    @NotBlank(message = "색상을 선택해주세요.")
    private String color;

    @Positive(message = "수량은 1개 이상이어야 합니다.")
    private int quantity;
}
