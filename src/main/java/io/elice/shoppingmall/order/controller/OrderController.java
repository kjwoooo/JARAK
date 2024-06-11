package io.elice.shoppingmall.order.controller;

import io.elice.shoppingmall.member.entity.Member;
import io.elice.shoppingmall.member.service.MemberService;
import io.elice.shoppingmall.order.dto.OrderDTO;
import io.elice.shoppingmall.order.dto.OrderDetailDTO;
import io.elice.shoppingmall.order.service.OrderService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;
    private final MemberService memberService;

    @Autowired
    public OrderController(OrderService orderService, MemberService memberService) {
        this.orderService = orderService;
        this.memberService = memberService;
    }

    // 주문 내역 조회
    @GetMapping
    public ResponseEntity<Page<OrderDTO>> getOrders(@CookieValue String jwtToken,
                                                    @RequestParam(defaultValue = "0") int page,
                                                    @RequestParam(defaultValue = "10") int size) {
        Member member = memberService.findByJwtToken(jwtToken);
        Page<OrderDTO> orders = orderService.getOrdersByMemberId(member.getId(), page, size);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    // 주문 상세 내역 조회
    @GetMapping("/{orderId}")
    public ResponseEntity<List<OrderDetailDTO>> getOrderDetails(@CookieValue String jwtToken,
                                                                @PathVariable Long orderId) {
        Member member = memberService.findByJwtToken(jwtToken);
        List<OrderDetailDTO> orderDetails = orderService.getOrderDetailsByOrderId(orderId, member.getId());
        return new ResponseEntity<>(orderDetails, HttpStatus.OK);
    }

    // 주문 생성 페이지 호출
    @GetMapping("/create")
    public ResponseEntity<OrderDTO> getCreateOrderPage(@CookieValue String jwtToken) {
        Member member = memberService.findByJwtToken(jwtToken);

        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setMemberId(member.getId());
        return new ResponseEntity<>(orderDTO, HttpStatus.OK);
    }

    // 주문 생성
    @PostMapping("/create")
    public ResponseEntity<OrderDTO> createOrder(@CookieValue String jwtToken, @RequestBody OrderDTO orderDTO) {
        OrderDTO createdOrder = orderService.createOrder(jwtToken, orderDTO);
        return new ResponseEntity<>(createdOrder, HttpStatus.CREATED);
    }

    // 주문 수정 페이지 호출
    @GetMapping("/update/{orderId}")
    public ResponseEntity<OrderDTO> getUpdateOrderPage(@CookieValue String jwtToken, @PathVariable Long orderId) {
        Member member = memberService.findByJwtToken(jwtToken);
        OrderDTO orderDTO = orderService.getUpdateOrderPage(orderId, member.getId());
        return new ResponseEntity<>(orderDTO, HttpStatus.OK);
    }

    // 주문 수정
    @PutMapping("/update/{orderId}")
    public ResponseEntity<OrderDTO> updateOrder(@CookieValue String jwtToken, @PathVariable Long orderId,
                                                @RequestBody OrderDTO orderDTO) {
        OrderDTO updatedOrder = orderService.updateOrder(jwtToken, orderId, orderDTO);
        return new ResponseEntity<>(updatedOrder, HttpStatus.OK);
    }

    // 주문 삭제
    @DeleteMapping("/delete/{orderId}")
    public ResponseEntity<String> deleteOrder(@CookieValue String jwtToken, @PathVariable Long orderId) {
        orderService.deleteOrder(jwtToken, orderId);
        return new ResponseEntity<>("주문이 성공적으로 삭제되었습니다.", HttpStatus.OK);
    }
}
