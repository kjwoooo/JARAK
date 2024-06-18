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
    private Long memberId;

    @NotNull(message = "Shipping cost is required.")
    private Integer shippingCost;

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
