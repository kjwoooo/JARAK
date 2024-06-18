package io.elice.shoppingmall.product.Controller;

import io.elice.shoppingmall.product.DTO.ReviewDTO;
import io.elice.shoppingmall.product.Service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/reviews")
public class ReviewController {
    private final ReviewService reviewService;

    // 리뷰 생성
    @PostMapping("/{itemId}")
    public ResponseEntity<ReviewDTO> createReview(@PathVariable Long itemId, @RequestBody ReviewDTO reviewDTO) {
        return new ResponseEntity<>(reviewService.createReview(itemId, reviewDTO), HttpStatus.CREATED);
    }

    // 리뷰 조회(페이지네이션)
    //FIX
    @GetMapping("/{itemId}")
//    public ResponseEntity<Page<ReviewDTO>> getReviews(@PathVariable Long itemId){
//                                                      ,@PageableDefault(size = 5) Pageable pageable) {
//        Page<ReviewDTO> reviews = reviewService.getReviews(itemId, pageable.getPageNumber(), pageable.getPageSize());
//       Page<ReviewDTO> reviews = reviewService.getReviews(itemId);
//        return new ResponseEntity<>(reviews, HttpStatus.OK);
    public ResponseEntity<List<ReviewDTO>> getReviews(@PathVariable Long itemId){
        List<ReviewDTO> reviewDTOs = reviewService.getReviews(itemId);
        return new ResponseEntity<>(reviewDTOs, HttpStatus.OK);
    }

    // 리뷰 수정
    @PutMapping("/{reviewId}")
    public ResponseEntity<ReviewDTO> updateReview(@PathVariable Long reviewId, @RequestBody ReviewDTO reviewDTO) {
        return new ResponseEntity<>(reviewService.updateReview(reviewId, reviewDTO), HttpStatus.OK);

    }

    // 리뷰 삭제
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long reviewId) {
        reviewService.deleteReview(reviewId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
