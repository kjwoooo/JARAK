package io.elice.shoppingmall.order.mapper;

import io.elice.shoppingmall.order.dto.OrderDetailDTO;
import io.elice.shoppingmall.order.entity.OrderDetail;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface OrderDetailMapper {
    OrderDetailMapper INSTANCE = Mappers.getMapper(OrderDetailMapper.class);

    OrderDetailDTO toOrderDetailDTO(OrderDetail orderDetail);
    OrderDetail toOrderDetail(OrderDetailDTO orderDetailDTO);
}