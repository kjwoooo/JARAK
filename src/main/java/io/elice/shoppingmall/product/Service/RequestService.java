package io.elice.shoppingmall.product.Service;

import io.elice.shoppingmall.product.DTO.ReplyDTO;
import io.elice.shoppingmall.product.DTO.RequestDTO;
import io.elice.shoppingmall.product.Entity.Item.Item;
import io.elice.shoppingmall.product.Entity.Review.Reply;
import io.elice.shoppingmall.product.Entity.Review.Request;
import io.elice.shoppingmall.product.Repository.Item.ItemRepository;
import io.elice.shoppingmall.product.Repository.RequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RequestService {
    private final RequestRepository requestRepository;
    private final ItemRepository itemRepository;


    //문의 생성
//    public ReplyDTO save(ReplyDTO replyDTO){
//        Reply
//    }
}
