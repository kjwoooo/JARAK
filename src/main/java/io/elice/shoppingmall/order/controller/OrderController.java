package io.elice.shoppingmall.order.controller;

import io.elice.shoppingmall.order.dto.OrderDTO;
import io.elice.shoppingmall.order.entity.Order;
import io.elice.shoppingmall.order.service.OrderService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // 사용자 Id로 주문 내역 조회
    @GetMapping("/{memberId}")
    public List<OrderDTO> getOrdersByMemberId(@PathVariable("memberId") Long memberId,
                                              @RequestParam(defaultValue = "0") int page,
                                              @RequestParam(defaultValue = "10") int size) {
        return orderService.getOrdersByMemberId(memberId, page, size);
    }
}
