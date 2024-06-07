package io.elice.shoppingmall.cart.controller;

import io.elice.shoppingmall.cart.domain.cart.Entity.Cart;
import io.elice.shoppingmall.cart.domain.cartItems.Entity.CartItems;
import io.elice.shoppingmall.cart.service.CartService;
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

    private final CartService cartService;

    //cart조회
    @GetMapping("/{cartId}")
    public ResponseEntity<Cart> getCartById(@PathVariable Long cartId) {
        try {
            Cart cart = cartService.getCartById(cartId);
            return new ResponseEntity<>(cart, HttpStatus.OK);
        } catch (ResourceAccessException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    //cart의 모든 item 조회
    @GetMapping("/{cartId}/items")
    public ResponseEntity<List<CartItems>> getCartItems(@PathVariable Long cartId) {
        try {
            List<CartItems> items = cartService.findItems(cartId);
            return new ResponseEntity<>(items, HttpStatus.OK);
        } catch (ResourceAccessException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

//    //create cart
//    @PostMapping
//    public ResponseEntity<Cart> createCart(@RequestBody Long memberId) {
//        Cart cart = cartService.createCart(memberId);
//        return new ResponseEntity<>(cart, HttpStatus.CREATED);
//    }

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

}
