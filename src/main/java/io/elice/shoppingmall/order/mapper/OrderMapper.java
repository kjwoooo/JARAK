package io.elice.shoppingmall.order.mapper;

import io.elice.shoppingmall.order.dto.OrderDTO;
import io.elice.shoppingmall.order.entity.Order;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface OrderMapper {
    // 매퍼 인터페이스의 인스턴스 생성
    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);

    // Order 엔티티를 OrderDTO로 매핑하는 메서드
    @Mapping(source = "member.id", target = "memberId") // Order 엔티티의 member 필드의 id 값을 OrderDTO의 memberId 필드에 매핑
    OrderDTO orderToOrderDTO(Order order);

    // OrderDTO를 Order 엔티티로 매핑하는 메서드
    @Mapping(source = "memberId", target = "member.id") // OrderDTO의 memberId 필드 값을 Order 엔티티의 member 필드의 id에 매핑
    Order orderDTOToOrder(OrderDTO orderDTO);

    // 여러 개의 Order 엔티티를 여러 개의 OrderDTO로 매핑하는 메서드
    List<OrderDTO> ordersToOrderDTOs(List<Order> orders);
}
