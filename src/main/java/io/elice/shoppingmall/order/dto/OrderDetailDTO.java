package io.elice.shoppingmall.order.dto;

import io.elice.shoppingmall.order.entity.Order;
import io.elice.shoppingmall.order.entity.OrderDetail;
import io.elice.shoppingmall.order.entity.OrderState;
import io.elice.shoppingmall.product.Entity.Item.Item;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDetailDTO {
    private Long id;
    private Long orderId;

    @NotNull(message = "상품 ID는 필수 항목입니다.")
    private Long itemId;

    @NotNull(message = "가격은 필수 항목입니다.")
    @Min(value = 0, message = "가격은 0 이상이어야 합니다.")
    private Integer price;

    @NotNull(message = "수량은 필수 항목입니다.")
    @Min(value = 1, message = "수량은 1 이상이어야 합니다.")
    private Integer quantity;

    @NotBlank(message = "색상은 필수 항목입니다.")
    private String color;

    @NotBlank(message = "사이즈는 필수 항목입니다.")
    private String size;

    private OrderState orderState;

    public OrderDetail toEntity(Order order, Item item) {
        return OrderDetail.builder()
                .id(id)
                .order(order)
                .item(item)
                .price(price)
                .quantity(quantity)
                .color(color)
                .size(size)
                .orderState(orderState)
                .build();
    }
}

