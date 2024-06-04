package io.elice.shoppingmall.order.service;

import io.elice.shoppingmall.order.entity.Order;
import io.elice.shoppingmall.order.repository.OrderRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService {
    private final OrderRepository orderRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public List<Order> getOrdersByMemberId(Long memberId) {
        return orderRepository.findByMemberIdOrderByIdDesc(memberId);
    }
}
