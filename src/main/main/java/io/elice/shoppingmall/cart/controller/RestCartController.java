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
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import org.springframework.web.client.ResourceAccessException;

@RestController
@AllArgsConstructor
@RequestMapping("/carts")
@Validated
public class RestCartController {
    private final CartService cartService;
    private final CartItemService cartItemService;
    private final MemberService memberService;

    // 특정 cart 조회
    @GetMapping("/{cartId}")
    public ResponseEntity<CartResponseDto> getCartById(@PathVariable Long cartId, @AuthenticationPrincipal UserDetails userDetails) {
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
    @GetMapping("/{cartId}/items")
    public ResponseEntity<List<CartItemResponseDto>> getCartItems(@PathVariable Long cartId, @AuthenticationPrincipal UserDetails userDetails) {
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
    @PostMapping
    public ResponseEntity<CartResponseDto> createCart(@AuthenticationPrincipal UserDetails userDetails) {
        Member member = memberService.findByUsername(userDetails.getUsername());
        CartResponseDto cart = cartService.createCart(member.getId());
        return new ResponseEntity<>(cart, HttpStatus.CREATED);
    }

    // 특정 cart 삭제 (로그인한 사용자의 카트만 삭제)
    @DeleteMapping("/{cartId}")
    public ResponseEntity<Void> deleteCart(@PathVariable Long cartId, @AuthenticationPrincipal UserDetails userDetails) {
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
    @PostMapping("/{cartId}/items/{itemId}")
    public ResponseEntity<CartItemAddResponseDto> addCartItem(@PathVariable Long cartId, @PathVariable Long itemId,
        @Valid @RequestBody CartItemRequestDto cartItemRequestDto,
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
    @DeleteMapping("/{cartId}/items/{cartItemId}")
    public ResponseEntity<Void> deleteCartItem(@PathVariable Long cartId, @PathVariable Long cartItemId,
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
    @PutMapping("/{cartId}/items/{cartItemId}")
    public ResponseEntity<CartItemAddResponseDto> updateCartItemQuantity(@PathVariable Long cartId,
        @PathVariable Long cartItemId,
        @RequestParam int quantity,
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
