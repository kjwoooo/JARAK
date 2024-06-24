package io.elice.shoppingmall.cart.controller;

import io.elice.shoppingmall.cart.domain.cart.DTO.CartResponseDto;
import io.elice.shoppingmall.cart.domain.cartItems.DTO.CartItemRequestDto;
import io.elice.shoppingmall.cart.domain.cartItems.DTO.CartItemAddResponseDto;
import io.elice.shoppingmall.cart.domain.cartItems.DTO.CartItemResponseDto;
import io.elice.shoppingmall.cart.domain.cart.Entity.Cart;
import io.elice.shoppingmall.cart.service.CartItemService;
import io.elice.shoppingmall.cart.service.CartService;
import io.elice.shoppingmall.member.entity.Member;
import io.elice.shoppingmall.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import org.springframework.web.client.ResourceAccessException;

@RestController
@AllArgsConstructor
@RequestMapping("/carts")
@Validated
@Tag(name = "장바구니 관리", description = "장바구니 관련 API")
public class RestCartController {
    private final CartService cartService;
    private final CartItemService cartItemService;
    private final MemberService memberService;

    // 특정 cart 조회
    @Operation(summary = "특정 장바구니 조회", description = "로그인한 회원의 특정 장바구니를 조회합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "조회 성공"),
        @ApiResponse(responseCode = "400", description = "회원 정보가 존재하지 않습니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "404", description = "NOT FOUND")
    })
    @GetMapping("/{cartId}")
    public ResponseEntity<CartResponseDto> getCartById(
        @Parameter(name = "cartId", description = "장바구니 고유 번호", in = ParameterIn.PATH) @RequestParam(defaultValue = "0") @PathVariable Long cartId,
        @AuthenticationPrincipal UserDetails userDetails) {
        try {
            Member member = memberService.findByUsername(userDetails.getUsername());
            Cart cart = cartService.findCartByMemberId(member);
            if (cart.getId() == cartId) {
                CartResponseDto cartResponseDto = cartService.findById(cartId);
                return new ResponseEntity<>(cartResponseDto, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        } catch (ResourceAccessException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // 특정 cart의 모든 item 조회
    @Operation(summary = "특정 장바구니의 모든 상픔 조회", description = "로그인한 회원의 특정 장바구니의 모든 상품을 조회합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "조회 성공"),
        @ApiResponse(responseCode = "400", description = "회원 정보가 존재하지 않습니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "404", description = "NOT FOUND")
    })
    @GetMapping("/{cartId}/items")
    public ResponseEntity<List<CartItemResponseDto>> getCartItems(
        @Parameter(name = "cartId", description = "장바구니 고유 번호", in = ParameterIn.PATH) @PathVariable Long cartId,
        @AuthenticationPrincipal UserDetails userDetails) {
        try {
            Member member = memberService.findByUsername(userDetails.getUsername());
            Cart cart = cartService.findCartByMemberId(member);
            if (cart.getId() == cartId) {
                List<CartItemResponseDto> items = cartItemService.findAllItemsByCartId(cartId);
                return new ResponseEntity<>(items, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        } catch (ResourceAccessException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // 카트 생성 (로그인 한 사용자가 자신의 카트를 생성)
    @Operation(summary = "특정 장바구니의 모든 상픔 조회", description = "로그인한 회원의 특정 장바구니의 모든 상품을 조회합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "생성 성공"),
        @ApiResponse(responseCode = "400", description = "회원 정보가 존재하지 않습니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    @PostMapping
    public ResponseEntity<CartResponseDto> createCart(@AuthenticationPrincipal UserDetails userDetails) {
        Member member = memberService.findByUsername(userDetails.getUsername());
        CartResponseDto cart = cartService.createCart(member.getId());
        return new ResponseEntity<>(cart, HttpStatus.CREATED);
    }

    // 특정 cart 삭제 (로그인한 사용자의 카트만 삭제)
    @Operation(summary = "특정 장바구니 삭제", description = "로그인한 회원의 특정 장바구니를 삭제합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "삭제 성공"),
        @ApiResponse(responseCode = "400", description = "회원 정보가 존재하지 않습니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "404", description = "NOT FOUND")
    })
    @DeleteMapping("/{cartId}")
    public ResponseEntity<Void> deleteCart(
        @Parameter(name = "cartId", description = "장바구니 고유 번호", in = ParameterIn.PATH) @PathVariable Long cartId,
        @AuthenticationPrincipal UserDetails userDetails) {
        try {
            Member member = memberService.findByUsername(userDetails.getUsername());
            Cart cart = cartService.findCartByMemberId(member);
            if (cart.getId() == cartId) {
                cartService.deleteCart(cartId);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        } catch (ResourceAccessException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // 상품 추가
    // 옵션 null, quantity 0 불가능
    @Operation(summary = "특정 장바구니에 상품 추가", description = "로그인한 회원의 특정 장바구니에 상품을 추가합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "추가 성공"),
        @ApiResponse(responseCode = "400", description = "회원 정보가 존재하지 않습니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "404", description = "NOT FOUND")
    })
    @PostMapping("/{cartId}/items/{itemId}")
    public ResponseEntity<CartItemAddResponseDto> addCartItem(
        @Parameter(name = "cartId", description = "장바구니 고유 번호", in = ParameterIn.PATH) @PathVariable Long cartId,
        @Parameter(name = "itemId", description = "상품 고유 번호", in = ParameterIn.PATH) @PathVariable Long itemId,
        @Parameter(name = "cartItemRequestDto", description = "장바구니 상품 정보", in = ParameterIn.PATH) @Valid @RequestBody CartItemRequestDto cartItemRequestDto,
        @AuthenticationPrincipal UserDetails userDetails) {
        try {
            Member member = memberService.findByUsername(userDetails.getUsername());
            Cart cart = cartService.findCartByMemberId(member);
            if (cart.getId() == cartId) {
                CartItemAddResponseDto item = cartItemService.addCartItem(itemId, cartId, cartItemRequestDto);
                return new ResponseEntity<>(item, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        } catch (ResourceAccessException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // 상품 삭제
    @Operation(summary = "특정 장바구니에 상품 삭제", description = "로그인한 회원의 특정 장바구니에 상품을 삭제합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "추가 성공"),
        @ApiResponse(responseCode = "400", description = "회원 정보가 존재하지 않습니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "404", description = "NOT FOUND")
    })
    @DeleteMapping("/{cartId}/items/{cartItemId}")
    public ResponseEntity<Void> deleteCartItem(
        @Parameter(name = "cartId", description = "장바구니 고유 번호", in = ParameterIn.PATH) @PathVariable Long cartId,
        @Parameter(name = "cartItemId", description = "장바구니 상품 고유 번호", in = ParameterIn.PATH) @PathVariable Long cartItemId,
        @AuthenticationPrincipal UserDetails userDetails) {
        try {
            Member member = memberService.findByUsername(userDetails.getUsername());
            Cart cart = cartService.findCartByMemberId(member);
            if (cart.getId() == cartId) {
                cartItemService.deleteCartItem(cartItemId);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        } catch (ResourceAccessException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // 상품 수량 조절
    @Operation(summary = "특정 장바구니에 상품 수량 조절", description = "로그인한 회원의 특정 장바구니에 상품의 수량을 수정합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "추가 성공"),
        @ApiResponse(responseCode = "400", description = "회원 정보가 존재하지 않습니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "404", description = "NOT FOUND")
    })
    @PutMapping("/{cartId}/items/{cartItemId}")
    public ResponseEntity<CartItemAddResponseDto> updateCartItemQuantity(
        @Parameter(name = "cartId", description = "장바구니 고유 번호", in = ParameterIn.PATH) @PathVariable Long cartId,
        @Parameter(name = "cartItemId", description = "장바구니 상품 고유 번호", in = ParameterIn.PATH) @PathVariable Long cartItemId,
        @Parameter(name = "quantity", description = "수정할 상품 개수", in = ParameterIn.PATH) @RequestParam int quantity,
        @AuthenticationPrincipal UserDetails userDetails) {
        try {
            Member member = memberService.findByUsername(userDetails.getUsername());
            Cart cart = cartService.findCartByMemberId(member);
            if (cart.getId() == cartId) {
                CartItemRequestDto cartItemRequestDto = new CartItemRequestDto();
                cartItemRequestDto.setQuantity(quantity);
                CartItemAddResponseDto updatedItem = cartItemService.updateCartItemQuantity(cartId, cartItemId, cartItemRequestDto);
                return new ResponseEntity<>(updatedItem, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        } catch (ResourceAccessException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
