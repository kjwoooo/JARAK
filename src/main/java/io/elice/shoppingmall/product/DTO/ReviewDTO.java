package io.elice.shoppingmall.product.DTO;

import io.elice.shoppingmall.product.Entity.Review.Request;
import io.elice.shoppingmall.product.Entity.Review.Review;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ReviewDTO {
    private Long id;
    private Long memberId;
    private Long itemId;
    private String username; // 작성자 아이디(추가)
    private String title;
    private String content;
    private String img;
    private Double rate;
    private LocalDateTime createdAt;


    // Review 엔티티를 기반으로 ReviewDTO 생성
    public static ReviewDTO fromEntity(Review review) {
        ReviewDTO reviewDTO = new ReviewDTO();
        reviewDTO.setId(review.getId());
        reviewDTO.setMemberId(review.getMember().getId()); // Member의 ID 설정
        reviewDTO.setItemId(review.getItem().getId()); // Item의 ID 설정
        reviewDTO.setUsername(review.getMember().getUsername()); // 작성자 아이디?닉네임 설정
        reviewDTO.setTitle(review.getTitle());
        reviewDTO.setContent(review.getContent());
        reviewDTO.setImg(review.getImg());
        reviewDTO.setRate(review.getRate());
        reviewDTO.setCreatedAt(review.getCreatedAt());
        return reviewDTO;
    }

    public Review toEntity() {
        Review review = new Review();
        // memberId와 itemId는 서비스단에서 설정
        review.setTitle(this.title);
        review.setContent(this.content);
        review.setImg(this.img);
        review.setRate(this.rate);
        return review;
    }
}
