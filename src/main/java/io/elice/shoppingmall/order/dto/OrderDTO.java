package io.elice.shoppingmall.order.dto;

import io.elice.shoppingmall.order.entity.Order;
import io.elice.shoppingmall.order.entity.OrderState;
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

    private Long memberId;

    private Integer price;

    @NotNull(message = "배송비는 필수 항목입니다.")
    private Integer shippingCost;

    private Integer totalQuantity;

    private String repItemName;

    private String repItemImage;

    private String refundReason;

    @NotBlank(message = "수령인 이름은 필수 항목입니다.")
    @Size(max = 255, message = "수령인 이름은 최대 255자까지 입력할 수 있습니다.")
    private String recipientName;

    @NotBlank(message = "우편번호는 필수 항목입니다.")
    @Size(max = 20, message = "우편번호는 최대 20자까지 입력할 수 있습니다.")
    private String zipcode;

    @NotBlank(message = "주소는 필수 항목입니다.")
    @Size(max = 255, message = "주소는 최대 255자까지 입력할 수 있습니다.")
    private String addr;

    private String addrDetail;

    @NotBlank(message = "수령인 전화번호는 필수 항목입니다.")
    @Size(max = 20, message = "수령인 전화번호는 최대 20자까지 입력할 수 있습니다.")
    private String recipientTel;

    @NotBlank(message = "배송지명은 필수 항목입니다.")
    @Size(max = 255, message = "배송지명은 최대 255자까지 입력할 수 있습니다.")
    private String addrName;

    private String deliveryReq;

    private OrderState orderState;

    private List<OrderDetailDTO> orderDetails;

    public Order toEntity() {
        return Order.builder()
                .shippingCost(shippingCost)
                .refundReason(refundReason)
                .recipientName(recipientName)
                .zipcode(zipcode)
                .addr(addr)
                .addrDetail(addrDetail)
                .recipientTel(recipientTel)
                .addrName(addrName)
                .deliveryReq(deliveryReq)
                .build();
    }
}
