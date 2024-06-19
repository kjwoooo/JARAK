package io.elice.shoppingmall.product.Controller;

import io.elice.shoppingmall.member.entity.Member;
import io.elice.shoppingmall.member.service.MemberService;
import io.elice.shoppingmall.product.DTO.review.ReviewDTO;
import io.elice.shoppingmall.product.Entity.Review.Review;
import io.elice.shoppingmall.product.Service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private MemberService memberService;

    @PostMapping("/{itemId}")
    public ReviewDTO createReview(@PathVariable Long itemId, @RequestBody ReviewDTO reviewDTO, @AuthenticationPrincipal UserDetails userDetails) {
        return reviewService.createReview(reviewDTO, userDetails.getUsername(), itemId);
    }

    @PutMapping("/{reviewId}")
    public ReviewDTO updateReview(@PathVariable Long reviewId, @AuthenticationPrincipal UserDetails userDetails, @RequestBody ReviewDTO reviewDTO) {
        return reviewService.updateReview(reviewId, reviewDTO, userDetails.getUsername());
    }

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