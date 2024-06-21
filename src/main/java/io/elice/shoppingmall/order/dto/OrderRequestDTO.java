package io.elice.shoppingmall.order.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
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
public class OrderRequestDTO {

    @NotNull(message = "OrderDTO는 필수 항목입니다.")
    @Valid
    private OrderDTO orderDTO;

    @NotNull(message = "OrderDetails 리스트는 필수 항목입니다.")
    @Valid
    private List<OrderDetailDTO> orderDetailDTOs;
}
