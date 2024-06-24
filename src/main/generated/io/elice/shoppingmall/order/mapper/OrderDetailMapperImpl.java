package io.elice.shoppingmall.order.mapper;

import io.elice.shoppingmall.order.dto.OrderDetailDTO;
import io.elice.shoppingmall.order.entity.Order;
import io.elice.shoppingmall.order.entity.OrderDetail;
import io.elice.shoppingmall.product.Entity.Item.Item;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-06-19T23:19:02+0900",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.10 (Oracle Corporation)"
)
@Component
public class OrderDetailMapperImpl implements OrderDetailMapper {

    @Override
    public OrderDetailDTO orderDetailToOrderDetailDTO(OrderDetail orderDetail) {
        if ( orderDetail == null ) {
            return null;
        }

        OrderDetailDTO.OrderDetailDTOBuilder orderDetailDTO = OrderDetailDTO.builder();

        orderDetailDTO.orderId( orderDetailOrderId( orderDetail ) );
        orderDetailDTO.itemId( orderDetailItemId( orderDetail ) );
        orderDetailDTO.id( orderDetail.getId() );
        orderDetailDTO.price( orderDetail.getPrice() );
        orderDetailDTO.quantity( orderDetail.getQuantity() );
        orderDetailDTO.color( orderDetail.getColor() );
        orderDetailDTO.size( orderDetail.getSize() );
        orderDetailDTO.orderState( orderDetail.getOrderState() );

        return orderDetailDTO.build();
    }

    @Override
    public OrderDetail orderDetailDTOToOrderDetail(OrderDetailDTO orderDetailDTO) {
        if ( orderDetailDTO == null ) {
            return null;
        }

        OrderDetail.OrderDetailBuilder orderDetail = OrderDetail.builder();

        orderDetail.order( orderDetailDTOToOrder( orderDetailDTO ) );
        orderDetail.item( orderDetailDTOToItem( orderDetailDTO ) );
        orderDetail.id( orderDetailDTO.getId() );
        orderDetail.price( orderDetailDTO.getPrice() );
        orderDetail.quantity( orderDetailDTO.getQuantity() );
        orderDetail.color( orderDetailDTO.getColor() );
        orderDetail.size( orderDetailDTO.getSize() );
        orderDetail.orderState( orderDetailDTO.getOrderState() );

        return orderDetail.build();
    }

    @Override
    public List<OrderDetailDTO> orderDetailsToOrderDetailDTOs(List<OrderDetail> orderDetails) {
        if ( orderDetails == null ) {
            return null;
        }

        List<OrderDetailDTO> list = new ArrayList<OrderDetailDTO>( orderDetails.size() );
        for ( OrderDetail orderDetail : orderDetails ) {
            list.add( orderDetailToOrderDetailDTO( orderDetail ) );
        }

        return list;
    }

    private Long orderDetailOrderId(OrderDetail orderDetail) {
        if ( orderDetail == null ) {
            return null;
        }
        Order order = orderDetail.getOrder();
        if ( order == null ) {
            return null;
        }
        Long id = order.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private Long orderDetailItemId(OrderDetail orderDetail) {
        if ( orderDetail == null ) {
            return null;
        }
        Item item = orderDetail.getItem();
        if ( item == null ) {
            return null;
        }
        Long id = item.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    protected Order orderDetailDTOToOrder(OrderDetailDTO orderDetailDTO) {
        if ( orderDetailDTO == null ) {
            return null;
        }

        Order.OrderBuilder order = Order.builder();

        order.id( orderDetailDTO.getOrderId() );

        return order.build();
    }

    protected Item orderDetailDTOToItem(OrderDetailDTO orderDetailDTO) {
        if ( orderDetailDTO == null ) {
            return null;
        }

        Item item = new Item();

        item.setId( orderDetailDTO.getItemId() );

        return item;
    }
}
