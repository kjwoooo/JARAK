package io.elice.shoppingmall.product.Service;

import io.elice.shoppingmall.product.DTO.ReviewDTO;
import io.elice.shoppingmall.product.Entity.Item.Item;
import io.elice.shoppingmall.product.Entity.Review.Review;
import io.elice.shoppingmall.product.Repository.ItemRepository;
import io.elice.shoppingmall.product.Repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final ItemRepository itemRepository;
    Integer memeberId = 1;

    //댓글 생성
    public Integer save(ReviewDTO reviewDTO){
        Optional<Item> optionalItem = itemRepository.findById(reviewDTO.getId());
        if(optionalItem.isPresent()){
            Item item = optionalItem.get();
            Review review = Review.toSaveEntity(reviewDTO, item);
            return reviewRepository.save(review).getId();
        }
        else{
            return null;
        }
    }

    //전체 댓글 조회
    public List<ReviewDTO> findAll(Integer itemId){
        Item item = itemRepository.findById(itemId).get();
        List<Review> reviewList = reviewRepository.findAllByItemOrderByIdAsc(item);
        List<ReviewDTO> reviewDTOList = new ArrayList<>();
        for(Review review : reviewList){
            ReviewDTO reviewDTO = ReviewDTO.toReviewDTO(review, memeberId, itemId);
            reviewDTOList.add(reviewDTO);
        }
        return reviewDTOList;
    }
}
