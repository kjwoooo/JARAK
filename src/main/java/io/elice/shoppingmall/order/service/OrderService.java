package io.elice.shoppingmall.order.service;

import io.elice.shoppingmall.order.dto.OrderDTO;
import io.elice.shoppingmall.order.dto.OrderDetailDTO;
import io.elice.shoppingmall.order.entity.Order;
import io.elice.shoppingmall.order.entity.OrderDetail;
import io.elice.shoppingmall.order.mapper.OrderDetailMapper;
import io.elice.shoppingmall.order.mapper.OrderMapper;
import io.elice.shoppingmall.order.repository.OrderRepository;
import io.elice.shoppingmall.security.JwtTokenUtil;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper = OrderMapper.INSTANCE;
    private final OrderDetailMapper orderDetailMapper = OrderDetailMapper.INSTANCE;
    private final JwtTokenUtil jwtUtil;

    @Autowired
    public OrderService(OrderRepository orderRepository, JwtTokenUtil jwtUtil) {
        this.orderRepository = orderRepository;
        this.jwtUtil = jwtUtil;
    }

    // 주문 내역 조회 (페이징 적용)
    public Page<OrderDTO> getOrdersByMemberId(Long memberId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Order> orders = orderRepository.findByMemberIdOrderByIdDesc(memberId, pageable);
        return orders.map(orderMapper::orderToOrderDTO);
    }

    // 주문 상세 조회
    public List<OrderDetailDTO> getOrderDetailsByOrderId(Long orderId, Long memberId) {
        Optional<Order> orderOptional = orderRepository.findByIdAndMemberId(orderId, memberId);
        if(orderOptional.isPresent()) {
            Order order = orderOptional.get();
            List<OrderDetail> orderDetails = order.getOrderDetails();
            return orderDetailMapper.orderDetailsToOrderDetailDTOs(orderDetails);
        } else {
            throw new RuntimeException("주문을 찾을 수 없습니다.");
        }
    }

    // 주문 생성
    public OrderDTO createOrder(OrderDTO orderDTO) {
        Order order = orderMapper.orderDTOToOrder(orderDTO);
        order.setOrderDetails(orderDTO.getOrderDetails().stream()
                .map(orderDetailMapper::orderDetailDTOToOrderDetail)
                .collect(Collectors.toList()));
        Order savedOrder = orderRepository.save(order);
        return orderMapper.orderToOrderDTO(savedOrder);
    }
}
