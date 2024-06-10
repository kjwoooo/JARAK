package io.elice.shoppingmall.product.DTO;

import io.elice.shoppingmall.product.Entity.Review.Request;
import io.elice.shoppingmall.product.Entity.Review.Review;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class ReviewDTO {
    private Long id;
    private Long itemId;
    private Long memberId;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private String img;
    private Double rate;

    public static ReviewDTO toReviewDTO(Review review, Long memberId, Long itemId) {
        ReviewDTO reviewDTO = new ReviewDTO();
        reviewDTO.setId(review.getId());
        reviewDTO.setItemId(itemId);
        reviewDTO.setMemberId(memberId);
        reviewDTO.setTitle(review.getTitle());
        reviewDTO.setContent(review.getContent());
        reviewDTO.setCreatedAt(review.getCreatedAt());
        reviewDTO.setImg(review.getImg());
        reviewDTO.setRate(review.getRate());
        return reviewDTO;
    }
}
