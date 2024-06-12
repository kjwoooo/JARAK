package io.elice.shoppingmall.order.controller;

import io.elice.shoppingmall.exception.CustomException;
import io.elice.shoppingmall.member.entity.Member;
import io.elice.shoppingmall.member.service.MemberService;
import io.elice.shoppingmall.order.dto.OrderDTO;
import io.elice.shoppingmall.order.dto.OrderDetailDTO;
import io.elice.shoppingmall.order.service.OrderService;
import jakarta.validation.Valid;
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

    // 회원을 JWT 토큰으로부터 검색
    private Member getMemberFromJwtToken(String jwtToken) {
        return memberService.findByJwtToken(jwtToken);
    }

    // 주문 내역 조회
    @GetMapping
    public ResponseEntity<Page<OrderDTO>> getOrders(@CookieValue String jwtToken,
                                                    @RequestParam(defaultValue = "0") int page,
                                                    @RequestParam(defaultValue = "10") int size) {
        try {
            Member member = getMemberFromJwtToken(jwtToken);
            Page<OrderDTO> orders = orderService.getOrdersByMemberId(member.getId(), page, size);
            return new ResponseEntity<>(orders, HttpStatus.OK);
        } catch (CustomException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    // 주문 상세 내역 조회
    @GetMapping("/{orderId}")
    public ResponseEntity<List<OrderDetailDTO>> getOrderDetails(@CookieValue String jwtToken,
                                                                @PathVariable Long orderId) {
        try {
            Member member = getMemberFromJwtToken(jwtToken);
            List<OrderDetailDTO> orderDetails = orderService.getOrderDetailsByOrderId(orderId, member.getId());
            return new ResponseEntity<>(orderDetails, HttpStatus.OK);
        } catch (CustomException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    // 주문 생성 페이지 호출
    @GetMapping("/create")
    public ResponseEntity<OrderDTO> getCreateOrderPage(@CookieValue String jwtToken) {
        try {
            Member member = getMemberFromJwtToken(jwtToken);
            OrderDTO orderDTO = new OrderDTO();
            orderDTO.setMemberId(member.getId());
            return new ResponseEntity<>(orderDTO, HttpStatus.OK);
        } catch (CustomException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    // 주문 생성
    @PostMapping("/create")
    public ResponseEntity<OrderDTO> createOrder(@CookieValue String jwtToken, @Valid @RequestBody OrderDTO orderDTO) {
        try {
            OrderDTO createdOrder = orderService.createOrder(jwtToken, orderDTO);
            return new ResponseEntity<>(createdOrder, HttpStatus.CREATED);
        } catch (CustomException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    // 주문 수정 페이지 호출
    @GetMapping("/update/{orderId}")
    public ResponseEntity<OrderDTO> getUpdateOrderPage(@CookieValue String jwtToken, @PathVariable Long orderId) {
        try {
            Member member = getMemberFromJwtToken(jwtToken);
            OrderDTO orderDTO = orderService.getUpdateOrderPage(orderId, member.getId());
            return new ResponseEntity<>(orderDTO, HttpStatus.OK);
        } catch (CustomException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    // 주문 수정
    @PutMapping("/update/{orderId}")
    public ResponseEntity<OrderDTO> updateOrder(@CookieValue String jwtToken, @PathVariable Long orderId,
                                                @Valid @RequestBody OrderDTO orderDTO) {
        try {
            OrderDTO updatedOrder = orderService.updateOrder(jwtToken, orderId, orderDTO);
            return new ResponseEntity<>(updatedOrder, HttpStatus.OK);
        } catch (CustomException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    // 주문 삭제
    @DeleteMapping("/delete/{orderId}")
    public ResponseEntity<String> deleteOrder(@CookieValue String jwtToken, @PathVariable Long orderId) {
        try {
            orderService.deleteOrder(jwtToken, orderId);
            return new ResponseEntity<>("주문이 성공적으로 삭제되었습니다.", HttpStatus.OK);
        } catch (CustomException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }
}
