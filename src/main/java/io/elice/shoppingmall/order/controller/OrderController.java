package io.elice.shoppingmall.order.controller;

import io.elice.shoppingmall.member.entity.Member;
import io.elice.shoppingmall.member.service.MemberService;
import io.elice.shoppingmall.order.dto.OrderDTO;
import io.elice.shoppingmall.order.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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

    private Member getAuthenticatedMember(UserDetails userDetails) {
        return memberService.findByUsername(userDetails.getUsername());
    }

    // 주문 내역 조회
    @GetMapping
    public ResponseEntity<Page<OrderDTO>> getOrders(@AuthenticationPrincipal UserDetails userDetails,
                                                    @RequestParam(defaultValue = "0") int page,
                                                    @RequestParam(defaultValue = "10") int size) {
        Member member = getAuthenticatedMember(userDetails);
        Page<OrderDTO> orders = orderService.getOrdersByMemberId(member.getId(), page, size);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    // 주문 상세 내역 조회
    @GetMapping("/order/{orderId}")
    public ResponseEntity<OrderDTO> getOrderDetails(@AuthenticationPrincipal UserDetails userDetails,
                                                    @PathVariable Long orderId) {
        Member member = getAuthenticatedMember(userDetails);
        OrderDTO orderDetails = orderService.getOrderDetailsByOrderId(orderId, member.getId());
        return new ResponseEntity<>(orderDetails, HttpStatus.OK);
    }

    // 주문 생성
    @PostMapping
    public ResponseEntity<OrderDTO> createOrder(@AuthenticationPrincipal UserDetails userDetails,
                                                @Valid @RequestBody OrderDTO orderDTO) {
        Member member = getAuthenticatedMember(userDetails);
        OrderDTO createdOrder = orderService.createOrder(member, orderDTO);
        return new ResponseEntity<>(createdOrder, HttpStatus.CREATED);
    }

    // 주문 수정 페이지 호출
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDTO> getUpdateOrderPage(@AuthenticationPrincipal UserDetails userDetails,
                                                       @PathVariable Long orderId) {
        Member member = getAuthenticatedMember(userDetails);
        OrderDTO orderDTO = orderService.getUpdateOrderPage(orderId, member.getId());
        return new ResponseEntity<>(orderDTO, HttpStatus.OK);
    }

    // 주문 수정
    @PutMapping("/{orderId}")
    public ResponseEntity<OrderDTO> updateOrder(@AuthenticationPrincipal UserDetails userDetails,
                                                @PathVariable Long orderId,
                                                @Valid @RequestBody OrderDTO orderDTO) {
        Member member = getAuthenticatedMember(userDetails);
        OrderDTO updatedOrder = orderService.updateOrder(member, orderId, orderDTO);
        return new ResponseEntity<>(updatedOrder, HttpStatus.OK);
    }

    // 주문 취소(환불)
    @PostMapping("/{orderId}")
    public ResponseEntity<String> deleteOrder(@AuthenticationPrincipal UserDetails userDetails,
                                              @PathVariable Long orderId,
                                              @RequestParam(required = false) String refundReason) {
        Member member = getAuthenticatedMember(userDetails);
        orderService.cancelOrder(member, orderId, refundReason);
        return new ResponseEntity<>("주문이 성공적으로 취소되었습니다.", HttpStatus.OK);
    }
}
