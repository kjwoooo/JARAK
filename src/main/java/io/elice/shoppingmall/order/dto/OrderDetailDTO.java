package io.elice.shoppingmall.order.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailDTO {
    private Long id;
    private Long orderId;
    private Integer itemId;
    private Integer price;
    private Integer quantity;
}