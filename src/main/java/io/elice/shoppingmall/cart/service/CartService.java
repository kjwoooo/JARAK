package io.elice.shoppingmall.cart.service;

import io.elice.shoppingmall.cart.domain.cart.DTO.CartResponseDto;
import io.elice.shoppingmall.cart.domain.cart.Entity.Cart;
import io.elice.shoppingmall.cart.domain.cartItems.DTO.CartItemResponseDto;
import io.elice.shoppingmall.cart.domain.cartItems.Entity.CartItems;
import io.elice.shoppingmall.cart.repository.CartRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;

//전체적 cart service
@Service
public class CartService {

    public final CartRepository cartRepository;
    public final MemberReposito
    @Autowired
    public CartService(CartRepository cartRepository){
        this.cartRepository = cartRepository;
    }

//    //사용자가 상품 최초로 하나 추가 시에 카트가 생성됨
//    //create cart(memberid)
//    public Cart createCart(Long memberId) {
//        Cart cart = new Cart();
//        cart.setMemberId(memberId);
//        return cartRepository.save(cart);
//   }

    //cart id로 cart 조회
    public CartResponseDto getCartById(Long cartId){
        Cart cart = cartRepository.findById(cartId).get();
        return cart.toCartResponseDto();
    }

    //cart 모든 item 조회(cartid)
    public List<CartResponseDto> findItems(Long cartId) {
        List<Cart> cartItems = cartRepository.findAllById(cartId);
        return cartItems.stream().map(Cart::toCartResponseDto).collect(Collectors.toList());
    }

    //cart 삭제(cartid)
    public void deleteCart(Long cartId){
        if (!cartRepository.existsById(cartId)){
            throw new ResourceAccessException("Cart" + cartId + " 찾을 수 없음.");
        }
        cartRepository.deleteById(cartId);
    }
}
