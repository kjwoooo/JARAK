package io.elice.shoppingmall.order.controller;

import io.elice.shoppingmall.order.dto.OrderDTO;
import io.elice.shoppingmall.order.entity.OrderState;
import io.elice.shoppingmall.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/orders")
public class OrderAdminController {
    private final OrderService orderService;

    @Autowired
    public OrderAdminController(OrderService orderService) {
        this.orderService = orderService;
    }

    // 관리자 모든 주문 조회 (페이징 적용 및 검색)
    @GetMapping
    public ResponseEntity<Page<OrderDTO>> getAllOrders(@RequestParam(defaultValue = "0") int page,
                                                       @RequestParam(defaultValue = "10") int size,
                                                       @RequestParam(required = false) String username) {
        Page<OrderDTO> orders = orderService.getAllOrders(page, size, username);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    // 관리자 주문 상태 수정
    @PutMapping("/update/{orderId}")
    public ResponseEntity<OrderDTO> updateOrderStatus(@PathVariable Long orderId,
                                                      @RequestParam OrderState orderState) {
        OrderDTO updatedOrder = orderService.updateOrderStatus(orderId, orderState);
        return new ResponseEntity<>(updatedOrder, HttpStatus.OK);
    }

    // 관리자 주문 삭제
    @DeleteMapping("/delete/{orderId}")
    public ResponseEntity<String> deleteOrder(@PathVariable Long orderId) {
        orderService.deleteOrder(orderId);
        return new ResponseEntity<>("주문이 성공적으로 삭제되었습니다.", HttpStatus.OK);
    }
}
