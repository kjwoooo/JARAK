package io.elice.shoppingmall.cart.service;

import io.elice.shoppingmall.cart.domain.cart.Entity.Cart;
import io.elice.shoppingmall.cart.domain.cartItems.DTO.CartItemRequestDto;
import io.elice.shoppingmall.cart.domain.cartItems.DTO.CartItemAddResponseDto;
import io.elice.shoppingmall.cart.domain.cartItems.DTO.CartItemResponseDto;
import io.elice.shoppingmall.cart.domain.cartItems.Entity.CartItems;
import io.elice.shoppingmall.cart.repository.CartItemRepository;
import io.elice.shoppingmall.cart.repository.CartRepository;
import io.elice.shoppingmall.product.Entity.Item.Item;
import io.elice.shoppingmall.product.Repository.Item.ItemRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CartItemService {
    public final CartItemRepository cartItemRepository;
    public final CartRepository cartRepository;

    public final ItemRepository itemRepository;

    @Autowired
    public CartItemService(CartItemRepository cartItemRepository, CartRepository cartRepository, ItemRepository itemRepository){
        this.cartItemRepository = cartItemRepository;
        this.cartRepository = cartRepository;
        this.itemRepository = itemRepository;
    }

    //cart 모든 item 조회(cartid)

    public List<CartItemResponseDto> findAllItemsByCartId(Long cartId) {
        List<CartItemResponseDto> responseDtoList = new ArrayList<>();
        List<Object[]> results = cartItemRepository.findAllItemsByCartId(cartId);

        for (Object[] result : results) {
            CartItemResponseDto responseDto = new CartItemResponseDto();
            responseDto.setId((Long) result[0]);
            responseDto.setSize((String) result[1]);
            responseDto.setColor((String) result[2]);
            responseDto.setQuantity((Integer) result[3]);
            responseDto.setItem_id((Long) result[4]);
            responseDto.setItem_name((String) result[5]);
            responseDto.setPrice((Integer) result[6]);

            responseDtoList.add(responseDto);
        }

        return responseDtoList;
    }

    public List<CartItems> findItemsentityList(Long cartId) {
        List<CartItemResponseDto> dtoList = findAllItemsByCartId(cartId);
        Cart cart = cartRepository.findById(cartId).get();
        List<CartItems> entityList = new ArrayList<>();

        for (CartItemResponseDto dto : dtoList) {
            Item item = itemRepository.findById(dto.getItem_id()).get();
            entityList.add(dto.toEntity(cart, item));
        }

        return entityList;
    }


//
//    //특정 item 조회
//    public  CartItemResponseDto findItemById(Long cartItemId) {
//        CartItems cartitem = cartItemRepository.
//    }

    //addCartitem
    //특정 item을 "장바구니에 추가" 했을 때 cartitemlist에 추가되는 로직
    public CartItemAddResponseDto addCartItem(Long itemId, Long cartId, CartItemRequestDto cartItemRequestDto) {
        //***id longtype으로 수정 요청
        Item item_id = itemRepository.findById(itemId).get();
        Cart cart_id = cartRepository.findById(cartId).get();
        CartItems cartitem = new CartItems().builder()
            .item_id(item_id)
            .cart_id(cart_id)
            .quantity(cartItemRequestDto.getQuantity())
            .build();

        cartItemRepository.save(cartitem);
        return cartitem.toCartItemAddResponseDto();

    }

    //deleteCartitem
    public void deleteCartItem(Long itemId){
        cartItemRepository.deleteById(itemId);
    }


    //수량 조절
//    public void updateItemQuantity(Long itemId, int quantity) {
//        CartItem item = cartRepository.findById(itemId).orElseThrow(() -> new ItemNotFoundException("Item not found"));
//        item.setQuantity(quantity);
//        cartRepository.save(item);
//    }
}
    //각 상품에 대한 총합 계산
