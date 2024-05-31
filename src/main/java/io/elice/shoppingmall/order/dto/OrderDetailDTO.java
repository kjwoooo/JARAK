package io.elice.shoppingmall.order.dto;

import io.elice.shoppingmall.order.entity.Order;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailDTO {
    private Long id;
    private Order order;
    private Integer itemId;
    private Integer price;
    private Integer quantity;
}