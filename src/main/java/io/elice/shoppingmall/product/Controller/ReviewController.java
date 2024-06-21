package io.elice.shoppingmall.product.Controller;

import io.elice.shoppingmall.member.entity.Member;
import io.elice.shoppingmall.member.service.MemberService;
import io.elice.shoppingmall.product.DTO.review.ReviewDTO;
import io.elice.shoppingmall.product.Entity.Review.Review;
import io.elice.shoppingmall.product.Service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/reviews")
@Tag(name = "리뷰 관리", description = "리뷰 관련 API")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private MemberService memberService;

    @Operation(summary = "새로운 리뷰 생성", description = "로그인한 회원이 상품에 대한 리뷰 작성을 요청합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "작성 성공"),
    })
    @PostMapping("/{itemId}")
    public ReviewDTO createReview(@PathVariable Long itemId, @RequestBody ReviewDTO reviewDTO, @AuthenticationPrincipal UserDetails userDetails) {
        return reviewService.createReview(reviewDTO, userDetails.getUsername(), itemId);
    }

    @Operation(summary = "리뷰 수정", description = "로그인한 회원이 작성했던 리뷰를 수정합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "수정 성공"),
    })
    @PutMapping("/{reviewId}")
    public ReviewDTO updateReview(@PathVariable Long reviewId, @AuthenticationPrincipal UserDetails userDetails, @RequestBody ReviewDTO reviewDTO) {
        return reviewService.updateReview(reviewId, reviewDTO, userDetails.getUsername());
    }

    @Operation(summary = "리뷰 삭제", description = "상품에 대한 리뷰의 삭제를 요청합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "삭제 성공"),
    })
    @DeleteMapping("/{id}")
    public void deleteReview(@PathVariable Long id) {
        reviewService.deleteReview(id);
    }

    //특정상품의 모든 리뷰 조희
    @Operation(summary = "특정 삼품의 모든 리뷰 조회", description = "특정 상품에 대한 모든 리뷰를 조회합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "조회 성공"),
    })
    @GetMapping("/{itemId}")
    public List<ReviewDTO> getAllReviews(@PathVariable Long itemId) {
        return reviewService.getAllReviews(itemId);
    }

    @Operation(summary = "특정 삼품의 특정 리뷰 조회", description = "특정 상품에 대해 특정 리뷰를 조회합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "조회 성공"),
    })
    @GetMapping("/review/{id}")
    public ReviewDTO getReviewById(@PathVariable Long id) {
        return reviewService.getReviewById(id);
    }

    @Operation(summary = "로그인한 회원이 작성했던 모든 리뷰 조회", description = "로그인한 회원이 작성했던 모든 리뷰를 조회합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "조회 성공"),
    })
    @GetMapping("/myreview")
    public List<ReviewDTO> getAllReviewsByusername(@AuthenticationPrincipal UserDetails userDetails){
        return reviewService.getMyReviews(userDetails.getUsername());
    }
}