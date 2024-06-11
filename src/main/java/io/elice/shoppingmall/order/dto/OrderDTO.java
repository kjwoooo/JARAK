package io.elice.shoppingmall.order.dto;

import io.elice.shoppingmall.order.entity.Order;
import io.elice.shoppingmall.order.entity.OrderState;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
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
    private List<OrderDetailDTO> orderDetails;
    private boolean useNewAddress; // 새로운 배송지 사용 여부

    public Order toEntity() {
        return Order.builder()
                .id(id)
                .price(price)
                .orderDate(orderDate)
                .payCard(payCard)
                .orderState(orderState)
                .refundReason(refundReason)
                .recipientName(recipientName)
                .zipcode(zipcode)
                .addr(addr)
                .addrDetail(addrDetail)
                .recipientTel(recipientTel)
                .deliveryReq(deliveryReq)
                .totalQuantity(totalQuantity)
                .repItemName(repItemName)
                .repItemImage(repItemImage)
                .build();
    }
}