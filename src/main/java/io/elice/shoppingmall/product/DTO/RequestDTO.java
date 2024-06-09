package io.elice.shoppingmall.product.DTO;

import io.elice.shoppingmall.product.Entity.Item.Item;
import io.elice.shoppingmall.product.Entity.Review.Request;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class RequestDTO {
    private Integer id;
    private Integer itemId;
    private Integer memberId;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private String img;
    private String commnet;

    public static RequestDTO toRequestDTO(Request request, Integer memberId, Integer itemId) {
        RequestDTO requestDTO = new RequestDTO();
        requestDTO.setId(request.getId());
        requestDTO.setItemId(itemId);
        requestDTO.setMemberId(memberId);
        requestDTO.setTitle(request.getTitle());
        requestDTO.setContent(request.getContent());
        requestDTO.setCreatedAt(request.getCreatedAt());
        requestDTO.setImg(request.getImg());
        requestDTO.setCommnet(request.getComment());
        return requestDTO;
    }
}
