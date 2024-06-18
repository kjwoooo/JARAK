package io.elice.shoppingmall.product.Service;

import io.elice.shoppingmall.product.DTO.RequestDTO;
import io.elice.shoppingmall.product.Entity.Item.Item;
import io.elice.shoppingmall.product.Entity.Review.Request;
import io.elice.shoppingmall.product.Repository.Item.ItemRepository;
import io.elice.shoppingmall.product.Repository.RequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RequestService {
    private final RequestRepository requestRepository;
    private final ItemRepository itemRepository;


//@GetMapping(@PathVariable itemId, @AuthenticationPrincipal UserDetails userDetails)
//    memberService.findbyusername(userDetail.getUsername()); //멤버 엔티티 반환
}
