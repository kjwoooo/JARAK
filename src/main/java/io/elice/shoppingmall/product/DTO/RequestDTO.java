package io.elice.shoppingmall.product.DTO;

import io.elice.shoppingmall.product.Entity.Item.Item;
import io.elice.shoppingmall.product.Entity.Review.Request;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Builder
public class RequestDTO {
    private Long id;
    private Long itemId;
    private Long memberId;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private String reply;

}
