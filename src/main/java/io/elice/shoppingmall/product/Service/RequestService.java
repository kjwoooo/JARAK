package io.elice.shoppingmall.product.Service;

import io.elice.shoppingmall.product.DTO.RequestDTO;
import io.elice.shoppingmall.product.DTO.ReviewDTO;
import io.elice.shoppingmall.product.Entity.Item.Item;
import io.elice.shoppingmall.product.Entity.Review.Request;
import io.elice.shoppingmall.product.Entity.Review.Review;
import io.elice.shoppingmall.product.Repository.ItemRepository;
import io.elice.shoppingmall.product.Repository.RequestRepository;
import io.elice.shoppingmall.product.Repository.ReviewRepository;
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
    Integer memberId = 1;

    //문의 생성
    public Integer save(RequestDTO requestDTO){
        Optional<Item> optionalItem = itemRepository.findById(requestDTO.getId());
        if(optionalItem.isPresent()){
            Item item = optionalItem.get();
            Request request = Request.toSaveEntity(requestDTO, item);
            return requestRepository.save(request).getId();
        }
        else{
            return null;
        }
    }

    //전체 문의 조회
    public List<RequestDTO> findAll(Integer itemId){
        Item item  = itemRepository.findById(itemId).get();
        List<Request> requestList = requestRepository.findAllByItemOrderByIdAsc(item);
        List<RequestDTO> requestDTOList = new ArrayList<>();
        for(Request request : requestList){
            RequestDTO requestDTO = RequestDTO.toRequestDTO(request, memberId, itemId);
            requestDTOList.add(requestDTO);
        }
        return requestDTOList;
    }
}
