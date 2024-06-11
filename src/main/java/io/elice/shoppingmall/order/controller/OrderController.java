package io.elice.shoppingmall.order.controller;

import io.elice.shoppingmall.member.entity.Member;
import io.elice.shoppingmall.member.service.MemberService;
import io.elice.shoppingmall.order.dto.OrderDTO;
import io.elice.shoppingmall.order.dto.OrderDetailDTO;
import io.elice.shoppingmall.order.entity.Order;
import io.elice.shoppingmall.order.service.OrderService;
import io.elice.shoppingmall.security.JwtTokenUtil;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    private final JwtTokenUtil jwtTokenUtil;
    private final MemberService memberService;

    // 주문 내역 조회
    @GetMapping
    public Page<OrderDTO> getOrders(HttpServletRequest request,
                                    @RequestParam(defaultValue = "0") int page,
                                    @RequestParam(defaultValue = "10") int size) {
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new RuntimeException("JWT 토큰이 존재하지 않습니다.");
        }

        String token = authorizationHeader.substring(7);
        if (jwtTokenUtil.isExpired(token, jwtTokenUtil.getSECRET_KEY())) {
            throw new RuntimeException("JWT 토큰이 만료되었습니다.");
        }

        String username = jwtTokenUtil.getUsername(token, jwtTokenUtil.getSECRET_KEY());
        Member member = memberService.findByUsername(username).orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        return orderService.getOrdersByMemberId(member.getId(), page, size);
    }

    // 주문 상세 내역 조회
    @GetMapping("/{orderId}")
    public List<OrderDetailDTO> getOrderDetails(HttpServletRequest request, @PathVariable Long orderId) {
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new RuntimeException("JWT 토큰이 존재하지 않습니다.");
        }

        String token = authorizationHeader.substring(7);
        if (jwtTokenUtil.isExpired(token, jwtTokenUtil.getSECRET_KEY())) {
            throw new RuntimeException("JWT 토큰이 만료되었습니다.");
        }

        String username = jwtTokenUtil.getUsername(token, jwtTokenUtil.getSECRET_KEY());
        Member member = memberService.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        return orderService.getOrderDetailsByOrderId(orderId, member.getId());
    }

    // 주문 생성 페이지 호출
    @GetMapping("/create")
    public ResponseEntity<OrderDTO> getCreateOrderPage(HttpServletRequest request) {
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new RuntimeException("JWT 토큰이 존재하지 않습니다.");
        }

        String token = authorizationHeader.substring(7);
        if (jwtTokenUtil.isExpired(token, jwtTokenUtil.getSECRET_KEY())) {
            throw new RuntimeException("JWT 토큰이 만료되었습니다.");
        }

        String username = jwtTokenUtil.getUsername(token, jwtTokenUtil.getSECRET_KEY());
        Member member = memberService.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setMemberId(member.getId());
        return new ResponseEntity<>(orderDTO, HttpStatus.OK);
    }

    // 주문 생성
    @PostMapping
    public ResponseEntity<OrderDTO> createOrder(HttpServletRequest request, @RequestBody OrderDTO orderDTO) {
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new RuntimeException("JWT 토큰이 존재하지 않습니다.");
        }

        String token = authorizationHeader.substring(7);
        if (jwtTokenUtil.isExpired(token, jwtTokenUtil.getSECRET_KEY())) {
            throw new RuntimeException("JWT 토큰이 만료되었습니다.");
        }

        String username = jwtTokenUtil.getUsername(token, jwtTokenUtil.getSECRET_KEY());
        Member member = memberService.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        orderDTO.setMemberId(member.getId());
        OrderDTO createdOrder = orderService.createOrder(orderDTO);
        return new ResponseEntity<>(createdOrder, HttpStatus.CREATED);
    }
}
