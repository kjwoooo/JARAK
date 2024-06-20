package io.elice.shoppingmall.order.controller;

import io.elice.shoppingmall.member.entity.Member;
import io.elice.shoppingmall.member.service.MemberService;
import io.elice.shoppingmall.order.dto.OrderDTO;
import io.elice.shoppingmall.order.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
@Tag(name = "주문 내역 관리", description = "주문 내역 관련 API")
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
    @Operation(summary = "모든 주문 내역 조회", description = "로그인한 회원의 모든 주문 내역을 조회합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "조회 성공"),
        @ApiResponse(responseCode = "400", description = "회원 정보가 존재하지 않습니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "400", description = "주문 내역이 존재하지 않습니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping
    public ResponseEntity<Page<OrderDTO>> getOrders(@AuthenticationPrincipal UserDetails userDetails,
        @Parameter(name = "page", description = "표시할 페이지 번호", in = ParameterIn.PATH) @RequestParam(defaultValue = "0") int page,
        @Parameter(name = "size", description = "페이지 크기", in = ParameterIn.PATH) @RequestParam(defaultValue = "10") int size) {
        Member member = getAuthenticatedMember(userDetails);
        Page<OrderDTO> orders = orderService.getOrdersByMemberId(member.getId(), page, size);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    // 주문 상세 내역 조회
    @Operation(summary = "특정 주문 내역 상세 조회", description = "로그인한 회원의 특정 주문 내역의 상세 정보를 조회합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "조회 성공"),
        @ApiResponse(responseCode = "400", description = "회원 정보가 존재하지 않습니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "400", description = "주문 내역이 존재하지 않습니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "400", description = "주문 내역 상세 정보가 존재하지 않습니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/order/{orderId}")
    public ResponseEntity<OrderDTO> getOrderDetails(@AuthenticationPrincipal UserDetails userDetails,
        @Parameter(name = "orderId", description = "주문 내역 고유 번호", in = ParameterIn.PATH) @PathVariable Long orderId) {
        Member member = getAuthenticatedMember(userDetails);
        OrderDTO orderDetails = orderService.getOrderDetailsByOrderId(orderId, member.getId());
        return new ResponseEntity<>(orderDetails, HttpStatus.OK);
    }

    // 주문 생성
    @Operation(summary = "주문 생성", description = "로그인한 회원의 주문 정보를 생성합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "조회 성공"),
        @ApiResponse(responseCode = "400", description = "회원 정보가 존재하지 않습니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "400", description = "장바구니가 비어있습니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping
    public ResponseEntity<OrderDTO> createOrder(@AuthenticationPrincipal UserDetails userDetails,
        @Parameter(name = "orderDTO", description = "주문 정보", in = ParameterIn.PATH) @Valid @RequestBody OrderDTO orderDTO) {
        Member member = getAuthenticatedMember(userDetails);
        OrderDTO createdOrder = orderService.createOrder(member, orderDTO);
        return new ResponseEntity<>(createdOrder, HttpStatus.CREATED);
    }

    // 주문 수정 페이지 호출
    @Operation(summary = "주문 내역 수정 페이지 호출", description = "로그인한 회원의 주문 내역 수정 페이지를 호출합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "조회 성공"),
        @ApiResponse(responseCode = "400", description = "회원 정보가 존재하지 않습니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "400", description = "주문 내역이 존재하지 않습니다", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDTO> getUpdateOrderPage(@AuthenticationPrincipal UserDetails userDetails,
        @Parameter(name = "orderId", description = "주문 내역 고유 번호", in = ParameterIn.PATH) @PathVariable Long orderId) {
        Member member = getAuthenticatedMember(userDetails);
        OrderDTO orderDTO = orderService.getUpdateOrderPage(orderId, member.getId());
        return new ResponseEntity<>(orderDTO, HttpStatus.OK);
    }

    // 주문 수정
    @Operation(summary = "주문 내역 수정", description = "로그인한 회원의 주문 내역을 수정합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "조회 성공"),
        @ApiResponse(responseCode = "400", description = "회원 정보가 존재하지 않습니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "400", description = "주문 내역이 존재하지 않습니다", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "400", description = "취소된 주문 내역은 수정할 수 없습니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "400", description = "발송 또는 배송 완료된 상품 내역은 수정할 수 없습니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PutMapping("/{orderId}")
    public ResponseEntity<OrderDTO> updateOrder(@AuthenticationPrincipal UserDetails userDetails,
        @Parameter(name = "orderId", description = "주문 내역 고유 번호", in = ParameterIn.PATH) @PathVariable Long orderId,
        @Parameter(name = "orderDTO", description = "주문 수정 내용", in = ParameterIn.PATH) @Valid @RequestBody OrderDTO orderDTO) {
        Member member = getAuthenticatedMember(userDetails);
        OrderDTO updatedOrder = orderService.updateOrder(member, orderId, orderDTO);
        return new ResponseEntity<>(updatedOrder, HttpStatus.OK);
    }

    // 주문 취소(환불)
    @Operation(summary = "주문 취소", description = "로그인한 회원의 상품 주문을 취소 합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "조회 성공"),
        @ApiResponse(responseCode = "400", description = "회원 정보가 존재하지 않습니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "400", description = "주문 내역이 존재하지 않습니다", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    @PatchMapping("/{orderId}")
    public ResponseEntity<String> deleteOrder(@AuthenticationPrincipal UserDetails userDetails,
        @Parameter(name = "orderId", description = "주문 내역 고유 번호", in = ParameterIn.PATH) @PathVariable Long orderId,
        @Parameter(name = "refundReason", description = "주문 취소 사유", in = ParameterIn.PATH) @RequestParam(required = false) String refundReason) {
        Member member = getAuthenticatedMember(userDetails);
        orderService.cancelOrder(member, orderId, refundReason);
        return new ResponseEntity<>("주문이 성공적으로 취소되었습니다.", HttpStatus.OK);
    }
}
