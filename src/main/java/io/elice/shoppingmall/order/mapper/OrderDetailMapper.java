package io.elice.shoppingmall.order.mapper;

import io.elice.shoppingmall.order.dto.OrderDetailDTO;
import io.elice.shoppingmall.order.entity.OrderDetail;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface OrderDetailMapper {
    OrderDetailMapper INSTANCE = Mappers.getMapper(OrderDetailMapper.class);

    @Mapping(source = "order.id", target = "orderId")
    @Mapping(source = "item.id", target = "itemId")
    OrderDetailDTO orderDetailToOrderDetailDTO(OrderDetail orderDetail);

    @Mapping(source = "orderId", target = "order.id")
    @Mapping(source = "itemId", target = "item.id")
    OrderDetail orderDetailDTOToOrderDetail(OrderDetailDTO orderDetailDTO);

    List<OrderDetailDTO> orderDetailsToOrderDetailDTOs(List<OrderDetail> orderDetails);
}
