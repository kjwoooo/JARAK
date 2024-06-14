package io.elice.shoppingmall.cart.service;

import io.elice.shoppingmall.cart.domain.cart.DTO.CartResponseDto;
import io.elice.shoppingmall.cart.domain.cart.Entity.Cart;
import io.elice.shoppingmall.cart.repository.CartRepository;
import io.elice.shoppingmall.member.entity.Member;
import io.elice.shoppingmall.member.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

//전체적 cart service
@Service
public class CartService {

    public final CartRepository cartRepository;
    public final MemberService memberService;
    @Autowired
    public CartService(CartRepository cartRepository, MemberService memberService){
        this.cartRepository = cartRepository;
        this.memberService= memberService;
    }

    //사용자 id에 따라 장바구니 생성
    // create cart(memberid)
    public CartResponseDto createCart(Long memberId) {
        Cart cart = new Cart();
        //해당 memberId 찾는 로직
        Member member_id = memberService.findByIdToMember(memberId);
        cart.setMember(member_id);
        cartRepository.save(cart);
        return cart.toCartResponseDto();
   }

   //member id로 cart 조회
   //entity로 변환해서 넘기기
   public Cart findCartByMemberId(Member member_id){
        return cartRepository.findByMember(member_id);
   }

    //cart id로 cart 조회
    public CartResponseDto findById(Long cartId){
        Cart cart = cartRepository.findById(cartId).get();
        return cart.toCartResponseDto();
    }

    //cart 삭제(cartid)
    public void deleteCart(Long cartId){
        cartRepository.deleteById(cartId);
    }
}
