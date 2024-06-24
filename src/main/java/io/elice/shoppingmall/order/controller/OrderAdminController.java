package io.elice.shoppingmall.order.controller;

import io.elice.shoppingmall.order.dto.OrderDTO;
import io.elice.shoppingmall.order.entity.OrderState;
import io.elice.shoppingmall.order.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/orders")
@Tag(name = "관리자용 주문 내역 관리", description = "관리자용 주문 내역 관련 API")
public class OrderAdminController {
    private final OrderService orderService;

    @Autowired
    public OrderAdminController(OrderService orderService) {
        this.orderService = orderService;
    }

    // 관리자 모든 주문 조회 (페이징 적용 및 검색)
    //Feedback: validator 적용하기
    @Operation(summary = "모든 주문 내역 조회", description = "모든 회원의 모든 주문내역 또는 특정 회원의 모든 주문 내역을 조회합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "조회 성공"),
        @ApiResponse(responseCode = "400", description = "주문 내역이 존재하지 않습니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping
    public ResponseEntity<Page<OrderDTO>> getAllOrders(
        @Parameter(name = "page", description = "표시할 페이지 번호", in = ParameterIn.PATH) @RequestParam(defaultValue = "0") int page,
        @Parameter(name = "size", description = "페이지 크기", in = ParameterIn.PATH) @RequestParam(defaultValue = "10") int size,
        @Parameter(name = "username", description = "조회할 회원 아이디", in = ParameterIn.PATH) @RequestParam(required = false) String username) {
        Page<OrderDTO> orders = orderService.getAllOrders(page, size, username);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    // 관리자 전체 주문 수 조회
    @Operation(summary = "모든 주문 내역 개수 조회", description = "현재 존재하는 모든 주문 내역의 개수를 조회합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "조회 성공"),
    })
    @GetMapping("/count")
    public ResponseEntity<Long> getTotalOrderCount() {
        long totalOrderCount = orderService.getTotalOrderCount();
        return new ResponseEntity<>(totalOrderCount, HttpStatus.OK);
    }

    // 관리자 주문 상태 수정
    @Operation(summary = "특정 주문 내역 상태 수정", description = "특정 주문 내역의 상태를 수정합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "수정 성공"),
    })
    @PutMapping("/{orderId}")
    public ResponseEntity<OrderDTO> updateOrderStatus(
        @Parameter(name = "orderId", description = "수정할 주문 내역 고유 번호", in = ParameterIn.PATH) @PathVariable Long orderId,
        @Parameter(name = "orderState", description = "수정할 주문 상태", in = ParameterIn.PATH) @RequestParam OrderState orderState) {
        OrderDTO updatedOrder = orderService.updateOrderStatus(orderId, orderState);
        return new ResponseEntity<>(updatedOrder, HttpStatus.OK);
    }

    // 관리자 주문 삭제
    @Operation(summary = "특정 주문 내역 삭제", description = "특정 주문 내역을 삭제합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "삭제 성공"),
    })
    @DeleteMapping("/{orderId}")
    public ResponseEntity<String> deleteOrder(
        @Parameter(name = "orderId", description = "삭제할 주문 고유 번호", in = ParameterIn.PATH) @PathVariable Long orderId) {
        orderService.deleteOrder(orderId);
        return new ResponseEntity<>("주문이 성공적으로 삭제되었습니다.", HttpStatus.OK);
    }
}
