package io.elice.shoppingmall.order.dto;

import io.elice.shoppingmall.order.entity.OrderState;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {
    private Long id;
    private Long memberId;
    private Integer price;
    private Date orderDate;
    private String payCard;
    private OrderState orderState;
    private String refundReason;
    private String recipientName;
    private String zipcode;
    private String addr;
    private String addrDetail;
    private String recipientTel;
    private String deliveryReq;
    private Integer totalQuantity;
    private String repItemName;
    private String repItemImage;
}
