package io.elice.shoppingmall.order.dto;

import io.elice.shoppingmall.order.entity.Order;
import io.elice.shoppingmall.order.entity.OrderDetail;
import io.elice.shoppingmall.product.Entity.Item.Item;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
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

    @NotNull(message = "Item ID is required.")
    private Long itemId;

    @NotNull(message = "Price is required.")
    @Positive(message = "Price must be positive.")
    private Integer price;

    @NotNull(message = "Quantity is required.")
    @Positive(message = "Quantity must be positive.")
    private Integer quantity;

    public OrderDetail toEntity(Order order, Item item) {
        return OrderDetail.builder()
                .id(id)
                .order(order)
                .item(item)
                .price(price)
                .quantity(quantity)
                .build();
    }
}
