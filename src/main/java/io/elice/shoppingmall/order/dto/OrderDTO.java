package io.elice.shoppingmall.order.dto;

import io.elice.shoppingmall.order.entity.Order;
import io.elice.shoppingmall.order.entity.OrderState;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;
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

    @NotNull(message = "Member ID is required.")
    private Long memberId;

    @NotNull(message = "Price is required.")
    private Integer price;

    // 기능 고도화 시 사용할 필드
    // @NotBlank(message = "Pay card is required.")
    // @Size(max = 255, message = "Pay card can have at most 255 characters.")
    // private String payCard;

    @NotNull(message = "Order state is required.")
    private OrderState orderState;

    private String refundReason;

    @NotBlank(message = "Recipient name is required.")
    @Size(max = 255, message = "Recipient name can have at most 255 characters.")
    private String recipientName;

    @NotBlank(message = "Zipcode is required.")
    @Size(max = 20, message = "Zipcode can have at most 20 characters.")
    private String zipcode;

    @NotBlank(message = "Address is required.")
    @Size(max = 255, message = "Address can have at most 255 characters.")
    private String addr;

    private String addrDetail;

    @NotBlank(message = "Recipient telephone is required.")
    @Size(max = 20, message = "Recipient telephone can have at most 20 characters.")
    private String recipientTel;

    @NotBlank(message = "Address name is required.")
    @Size(max = 255, message = "Address name can have at most 255 characters.")
    private String addrName;

    private String deliveryReq;

    @NotNull(message = "Total quantity is required.")
    private Integer totalQuantity;

    @NotBlank(message = "Representative item name is required.")
    @Size(max = 255, message = "Representative item name can have at most 255 characters.")
    private String repItemName;

    @NotBlank(message = "Representative item image is required.")
    @Size(max = 255, message = "Representative item image can have at most 255 characters.")
    private String repItemImage;

    @Valid  // 리스트 내부의 OrderDetailDTO도 유효성 검사 수행
    @NotNull(message = "Order details are required.")
    @Size(min = 1, message = "Order details must have at least one item.")
    private List<OrderDetailDTO> orderDetails;

    private Long selectedAddressId; // 사용자가 선택한 특정 주소 ID

    public Order toEntity() {
        return Order.builder()
                .id(id)
                .price(price)
                // .payCard(payCard)
                .orderState(orderState)
                .refundReason(refundReason)
                .recipientName(recipientName)
                .zipcode(zipcode)
                .addr(addr)
                .addrDetail(addrDetail)
                .recipientTel(recipientTel)
                .addrName(addrName)
                .deliveryReq(deliveryReq)
                .totalQuantity(totalQuantity)
                .repItemName(repItemName)
                .repItemImage(repItemImage)
                .build();
    }
}
