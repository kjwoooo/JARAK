package io.elice.shoppingmall.product.DTO.review;

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
    private Long itemId;
    private String username; // 작성자 아이디(추가)
    private String title;
    private String content;
    private Double rate;
    private String filePath;
    private LocalDateTime createdAt;
}
