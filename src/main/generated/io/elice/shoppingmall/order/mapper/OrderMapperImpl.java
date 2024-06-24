package io.elice.shoppingmall.order.mapper;

import io.elice.shoppingmall.member.entity.Member;
import io.elice.shoppingmall.order.dto.OrderDTO;
import io.elice.shoppingmall.order.dto.OrderDetailDTO;
import io.elice.shoppingmall.order.entity.Order;
import io.elice.shoppingmall.order.entity.OrderDetail;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-06-19T23:19:02+0900",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.10 (Oracle Corporation)"
)
public class OrderMapperImpl implements OrderMapper {

    @Override
    public OrderDTO orderToOrderDTO(Order order) {
        if ( order == null ) {
            return null;
        }

        OrderDTO.OrderDTOBuilder orderDTO = OrderDTO.builder();

        orderDTO.memberId( orderMemberId( order ) );
        orderDTO.id( order.getId() );
        orderDTO.price( order.getPrice() );
        orderDTO.shippingCost( order.getShippingCost() );
        orderDTO.totalQuantity( order.getTotalQuantity() );
        orderDTO.repItemName( order.getRepItemName() );
        orderDTO.repItemImage( order.getRepItemImage() );
        orderDTO.refundReason( order.getRefundReason() );
        orderDTO.recipientName( order.getRecipientName() );
        orderDTO.zipcode( order.getZipcode() );
        orderDTO.addr( order.getAddr() );
        orderDTO.addrDetail( order.getAddrDetail() );
        orderDTO.recipientTel( order.getRecipientTel() );
        orderDTO.addrName( order.getAddrName() );
        orderDTO.deliveryReq( order.getDeliveryReq() );
        orderDTO.orderState( order.getOrderState() );
        orderDTO.orderDetails( orderDetailListToOrderDetailDTOList( order.getOrderDetails() ) );

        return orderDTO.build();
    }

    @Override
    public Order orderDTOToOrder(OrderDTO orderDTO) {
        if ( orderDTO == null ) {
            return null;
        }

        Order.OrderBuilder order = Order.builder();

        order.member( orderDTOToMember( orderDTO ) );
        order.id( orderDTO.getId() );
        order.price( orderDTO.getPrice() );
        order.shippingCost( orderDTO.getShippingCost() );
        order.totalQuantity( orderDTO.getTotalQuantity() );
        order.repItemName( orderDTO.getRepItemName() );
        order.repItemImage( orderDTO.getRepItemImage() );
        order.refundReason( orderDTO.getRefundReason() );
        order.recipientName( orderDTO.getRecipientName() );
        order.zipcode( orderDTO.getZipcode() );
        order.addr( orderDTO.getAddr() );
        order.addrDetail( orderDTO.getAddrDetail() );
        order.recipientTel( orderDTO.getRecipientTel() );
        order.addrName( orderDTO.getAddrName() );
        order.deliveryReq( orderDTO.getDeliveryReq() );
        order.orderState( orderDTO.getOrderState() );
        order.orderDetails( orderDetailDTOListToOrderDetailList( orderDTO.getOrderDetails() ) );

        return order.build();
    }

    @Override
    public List<OrderDTO> ordersToOrderDTOs(List<Order> orders) {
        if ( orders == null ) {
            return null;
        }

        List<OrderDTO> list = new ArrayList<OrderDTO>( orders.size() );
        for ( Order order : orders ) {
            list.add( orderToOrderDTO( order ) );
        }

        return list;
    }

    private Long orderMemberId(Order order) {
        if ( order == null ) {
            return null;
        }
        Member member = order.getMember();
        if ( member == null ) {
            return null;
        }
        Long id = member.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    protected OrderDetailDTO orderDetailToOrderDetailDTO(OrderDetail orderDetail) {
        if ( orderDetail == null ) {
            return null;
        }

        OrderDetailDTO.OrderDetailDTOBuilder orderDetailDTO = OrderDetailDTO.builder();

        orderDetailDTO.id( orderDetail.getId() );
        orderDetailDTO.price( orderDetail.getPrice() );
        orderDetailDTO.quantity( orderDetail.getQuantity() );
        orderDetailDTO.color( orderDetail.getColor() );
        orderDetailDTO.size( orderDetail.getSize() );
        orderDetailDTO.orderState( orderDetail.getOrderState() );

        return orderDetailDTO.build();
    }

    protected List<OrderDetailDTO> orderDetailListToOrderDetailDTOList(List<OrderDetail> list) {
        if ( list == null ) {
            return null;
        }

        List<OrderDetailDTO> list1 = new ArrayList<OrderDetailDTO>( list.size() );
        for ( OrderDetail orderDetail : list ) {
            list1.add( orderDetailToOrderDetailDTO( orderDetail ) );
        }

        return list1;
    }

    protected Member orderDTOToMember(OrderDTO orderDTO) {
        if ( orderDTO == null ) {
            return null;
        }

        Member member = new Member();

        member.setId( orderDTO.getMemberId() );

        return member;
    }

    protected OrderDetail orderDetailDTOToOrderDetail(OrderDetailDTO orderDetailDTO) {
        if ( orderDetailDTO == null ) {
            return null;
        }

        OrderDetail.OrderDetailBuilder orderDetail = OrderDetail.builder();

        orderDetail.id( orderDetailDTO.getId() );
        orderDetail.price( orderDetailDTO.getPrice() );
        orderDetail.quantity( orderDetailDTO.getQuantity() );
        orderDetail.color( orderDetailDTO.getColor() );
        orderDetail.size( orderDetailDTO.getSize() );
        orderDetail.orderState( orderDetailDTO.getOrderState() );

        return orderDetail.build();
    }

    protected List<OrderDetail> orderDetailDTOListToOrderDetailList(List<OrderDetailDTO> list) {
        if ( list == null ) {
            return null;
        }

        List<OrderDetail> list1 = new ArrayList<OrderDetail>( list.size() );
        for ( OrderDetailDTO orderDetailDTO : list ) {
            list1.add( orderDetailDTOToOrderDetail( orderDetailDTO ) );
        }

        return list1;
    }
}
