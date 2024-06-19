package io.elice.shoppingmall.product.DTO.review;

import io.elice.shoppingmall.product.Entity.Item.Item;
import io.elice.shoppingmall.product.Entity.Review.Request;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RequestDTO {
    private Long id;
    private Long itemId;
    private Long memberId;
    private String title;
    private String content;
    private String username;
    private LocalDateTime createdAt;
    private List<ReplyDTO> replies;

}
