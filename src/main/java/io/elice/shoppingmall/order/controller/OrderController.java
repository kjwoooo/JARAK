package io.elice.shoppingmall.order.controller;

import io.elice.shoppingmall.order.dto.OrderDTO;
import io.elice.shoppingmall.order.dto.OrderDetailDTO;
import io.elice.shoppingmall.order.entity.Order;
import io.elice.shoppingmall.order.service.OrderService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/members")
public class OrderController {
    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // 사용자 Id로 주문 내역 조회
    @GetMapping("/{memberId}/orders")
    public List<OrderDTO> getOrdersByMemberId(@PathVariable("memberId") Long memberId,
                                              @RequestParam(defaultValue = "0") int page,
                                              @RequestParam(defaultValue = "10") int size) {
        return orderService.getOrdersByMemberId(memberId, page, size);
    }

    // 주문 생성
    @PostMapping("/{memberId}/orders")
    public ResponseEntity<OrderDTO> createOrder(@RequestBody OrderDTO orderDTO, @RequestBody List<OrderDetailDTO> orderDetailDTOs) {
        // 주문 생성 서비스 호출
        OrderDTO createdOrderDTO = orderService.createOrder(orderDTO, orderDetailDTOs);

        // 생성된 주문 정보와 HTTP 상태코드를 반환
        return new ResponseEntity<>(createdOrderDTO, HttpStatus.CREATED);
    }
}
