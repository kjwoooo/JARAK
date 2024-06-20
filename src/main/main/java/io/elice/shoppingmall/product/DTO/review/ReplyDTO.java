package io.elice.shoppingmall.product.DTO.review;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReplyDTO {
    private Long id;
    private String content;
    private Long RequestId;
}
