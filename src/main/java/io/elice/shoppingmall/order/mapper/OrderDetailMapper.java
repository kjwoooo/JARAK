package io.elice.shoppingmall.order.mapper;

import io.elice.shoppingmall.order.dto.OrderDetailDTO;
import io.elice.shoppingmall.order.entity.OrderDetail;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface OrderDetailMapper {
    // 매퍼 인터페이스의 인스턴스 생성
    OrderDetailMapper INSTANCE = Mappers.getMapper(OrderDetailMapper.class);

    // OrderDetail 엔티티를 OrderDetailDTO로 매핑하는 메서드
    @Mapping(source = "order.id", target = "orderId") // OrderDetail의 order 필드의 id 값을 OrderDetailDTO의 orderId 필드에 매핑
    @Mapping(source = "item.id", target = "itemId") // OrderDetail의 item 필드의 id 값을 OrderDetailDTO의 itemId 필드에 매핑
    OrderDetailDTO orderDetailToOrderDetailDTO(OrderDetail orderDetail);

    // OrderDetailDTO를 OrderDetail 엔티티로 매핑하는 메서드
    @Mapping(source = "orderId", target = "order.id") // OrderDetailDTO의 orderId 필드의 값을 OrderDetail의 order 필드의 id에 매핑
    @Mapping(source = "itemId", target = "item.id") // OrderDetailDTO의 itemId 필드의 값을 OrderDetail의 item 필드의 id에 매핑
    OrderDetail orderDetailDTOToOrderDetail(OrderDetailDTO orderDetailDTO);

    // 여러 개의 OrderDetail 엔티티를 여러 개의 OrderDetailDTO로 매핑하는 메서드
    List<OrderDetailDTO> orderDetailsToOrderDetailDTOs(List<OrderDetail> orderDetails);
}