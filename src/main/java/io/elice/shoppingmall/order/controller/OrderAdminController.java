package io.elice.shoppingmall.order.controller;

import io.elice.shoppingmall.order.dto.OrderDTO;
import io.elice.shoppingmall.order.service.OrderService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderAdminController {
    private final OrderService orderService;

    @Autowired
    public OrderAdminController(OrderService orderService) {
        this.orderService = orderService;
    }

    // 관리자용 주문 조회
    @GetMapping
    public ResponseEntity<List<OrderDTO>> getAllOrders(@RequestParam(defaultValue = "0") int page,
                                                       @RequestParam(defaultValue = "10") int size) {
        List<OrderDTO> orders = orderService.getAllOrders(page, size);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }
}
