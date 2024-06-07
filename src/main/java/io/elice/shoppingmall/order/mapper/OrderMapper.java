package io.elice.shoppingmall.order.mapper;

import io.elice.shoppingmall.order.dto.OrderDTO;
import io.elice.shoppingmall.order.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface OrderMapper {
    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);

    OrderDTO toOrderDTO(Order order);
    Order toOrder(OrderDTO orderDTO);
}