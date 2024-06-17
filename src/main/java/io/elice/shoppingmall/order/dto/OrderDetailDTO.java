package io.elice.shoppingmall.order.dto;

import io.elice.shoppingmall.order.entity.Order;
import io.elice.shoppingmall.order.entity.OrderDetail;
import io.elice.shoppingmall.order.entity.OrderState;
import io.elice.shoppingmall.product.Entity.Item.Item;
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
    private Long itemId;
    private Integer price;
    private Integer quantity;
    private String color;
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

