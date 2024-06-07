package io.elice.shoppingmall.cart.service;

import io.elice.shoppingmall.cart.repository.CartItemRepository;
import io.elice.shoppingmall.cart.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CartItemService {
    public final CartRepository cartRepository;
    public final CartItemRepository cartItemRepository;
    @Autowired
    public CartItemService(CartRepository cartRepository, CartItemRepository cartItemRepository){
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
    }

    //addCartitem


    //deleteCartitem
    //수량 조절
    //각 상품에 대한 총합 계산
}
