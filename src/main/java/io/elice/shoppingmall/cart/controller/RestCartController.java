package io.elice.shoppingmall.cart.controller;

import io.elice.shoppingmall.cart.domain.cart.DTO.CartResponseDto;
import io.elice.shoppingmall.cart.domain.cart.Entity.Cart;
import io.elice.shoppingmall.cart.domain.cartItems.DTO.CartItemRequestDto;
import io.elice.shoppingmall.cart.domain.cartItems.DTO.CartItemAddResponseDto;
import io.elice.shoppingmall.cart.domain.cartItems.DTO.CartItemResponseDto;
import io.elice.shoppingmall.cart.domain.cartItems.Entity.CartItems;
import io.elice.shoppingmall.cart.service.CartItemService;
import io.elice.shoppingmall.cart.service.CartService;
import io.elice.shoppingmall.member.entity.Member;
import io.elice.shoppingmall.member.service.MemberService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import org.springframework.web.client.ResourceAccessException;

@RestController
@AllArgsConstructor
@RequestMapping("/carts")
public class RestCartController {
    //로그인 된 상태에서
    private final CartService cartService;
    private final CartItemService cartItemService;
    private final MemberService memberService;

    //cart 조회
    @GetMapping("/{cartId}")
    public ResponseEntity<CartResponseDto> getCartById(@PathVariable Long cartId) {
        try {
            CartResponseDto cart = cartService.findById(cartId);
            return new ResponseEntity<>(cart, HttpStatus.OK);
        } catch (ResourceAccessException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    //cart의 모든 item 조회
    @GetMapping("/{cartId}/items")
    public ResponseEntity<List<CartItemResponseDto>> getCartItems(@PathVariable Long cartId) {
        try {
            List<CartItemResponseDto> items = cartItemService.findAllItemsByCartId(cartId);
            return new ResponseEntity<>(items, HttpStatus.OK);
        } catch (ResourceAccessException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }


    //카트 생성(회원이 로그인 시 생성)
    @PostMapping
    public ResponseEntity<CartResponseDto> createCart(@CookieValue String jwtToken) {
        //memberId 가져옴
        Member member = memberService.findByJwtToken(jwtToken);
        Long memberId = member.getId();
        CartResponseDto cart = cartService.createCart(memberId);
        return new ResponseEntity<>(cart, HttpStatus.CREATED);
    }

    // 특정 cart 삭제
    @DeleteMapping("/{cartId}")
    public ResponseEntity<Void> deleteCart(@PathVariable Long cartId) {
        try {
            cartService.deleteCart(cartId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (ResourceAccessException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    //상품 추가
    @PostMapping("/{cartId}/{itemId}")
    public ResponseEntity<CartItemAddResponseDto> addCartItem(@PathVariable Long cartId, @PathVariable Long itemId,
        @RequestBody CartItemRequestDto cartItemRequestDto)  {
        try {
            CartItemAddResponseDto item = cartItemService.addCartItem(itemId, cartId, cartItemRequestDto);
            return new ResponseEntity<>(item, HttpStatus.OK);
        } catch (ResourceAccessException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    //상품 삭제
    @DeleteMapping("/{cartId}/{itemId}")
    public ResponseEntity<Void> deleteCartItem(@PathVariable Long itemId) {
        try {
            cartItemService.deleteCartItem(itemId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (ResourceAccessException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    //상품 수량 조절
//    @PostMapping("/{cartId}/{itemId}/updateQuantity")
//    public ResponseEntity<String> updateQuantity(@RequestParam Long itemId, @RequestParam int quantity) {
//        try {
//            cartService.updateItemQuantity(itemId, quantity);
//            return ResponseEntity.ok("Quantity updated successfully");
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to update quantity");
//        }
//    }
}

