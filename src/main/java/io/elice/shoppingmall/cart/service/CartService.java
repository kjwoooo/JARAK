package io.elice.shoppingmall.cart.service;

import io.elice.shoppingmall.cart.domain.cart.Entity.Cart;
import io.elice.shoppingmall.cart.domain.cartItems.Entity.CartItems;
import io.elice.shoppingmall.cart.repository.CartRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;

//전체적 cart service
@Service
public class CartService {

    public final CartRepository cartRepository;

    @Autowired
    public CartService(CartRepository cartRepository){
        this.cartRepository = cartRepository;
    }


    //create cart(memberid)
//    public Cart createCart(Long memberId) {
//        Cart cart = new Cart();
//        cart.setMemberId(memberId);
//        return cartRepository.save(cart);
//    }

    //cart id로 cart 조회
    public Cart getCartById(Long cartId){
        return cartRepository.findById(cartId)
            .orElseThrow(() -> new ResourceAccessException("Cart" + cartId + " 찾을 수 없음."));
    }

    //cart 모든 item 조회(cartid)
    public List<CartItems> findItems(Long cartId) {
        Cart cart = getCartById(cartId);
        return cart.getCartItems();
    }

    //cart 삭제(cartid)
    public void deleteCart(Long cartId){
        if (!cartRepository.existsById(cartId)){
            throw new ResourceAccessException("Cart" + cartId + " 찾을 수 없음.");
        }
        cartRepository.deleteById(cartId);
    }

    //모든 상품 총합 계산(CalculateTotal)
}
