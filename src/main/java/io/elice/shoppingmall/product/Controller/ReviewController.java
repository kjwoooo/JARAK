package io.elice.shoppingmall.product.Controller;

import io.elice.shoppingmall.member.service.MemberService;
import io.elice.shoppingmall.product.DTO.Review.ReviewDTO;
import io.elice.shoppingmall.product.Service.Review.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


@RestController
@RequestMapping("/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private MemberService memberService;

    //review 생성
    @PostMapping("/{itemId}")
    public ReviewDTO createReview(@PathVariable Long itemId,
                                  @RequestPart ReviewDTO reviewDTO,
                                  @AuthenticationPrincipal UserDetails userDetails,
                                  @RequestPart(required = false) MultipartFile imageFile) throws IOException {
        return reviewService.createReview(reviewDTO, userDetails.getUsername(), itemId, imageFile);
    }

    //review 수정
    @PutMapping("/{reviewId}")
    public ReviewDTO updateReview(@PathVariable Long reviewId,
                                  @AuthenticationPrincipal UserDetails userDetails,
                                  @RequestPart ReviewDTO reviewDTO,
                                  @RequestPart(required = false) MultipartFile imageFile) throws IOException{
        return reviewService.updateReview(reviewId, reviewDTO, userDetails.getUsername(), imageFile);
    }

    //review 삭제
    @DeleteMapping("/{id}")
    public void deleteReview(@PathVariable Long id) {
        reviewService.deleteReview(id);
    }

    //특정상품의 모든 리뷰 조희
    @GetMapping("/{itemId}")
    public List<ReviewDTO> getAllReviews(@PathVariable Long itemId) {
        return reviewService.getAllReviews(itemId);
    }

    @GetMapping("/review/{id}")
    public ReviewDTO getReviewById(@PathVariable Long id) {
        return reviewService.getReviewById(id);
    }

    @GetMapping("/myreview")
    public List<ReviewDTO> getAllReviewsByusername(@AuthenticationPrincipal UserDetails userDetails){
        return reviewService.getMyReviews(userDetails.getUsername());
    }
}