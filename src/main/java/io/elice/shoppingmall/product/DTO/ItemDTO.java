package io.elice.shoppingmall.product.DTO;

import io.elice.shoppingmall.product.Entity.Item.Item;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class ItemDTO {
    private Long id;
    private Long memberId;
    private Long categoryId;
    private Long genderId;
    private Long itemImagesId;
    private Long brandId;
    private String itemName;
    private Integer price;
    private LocalDateTime createdAt;

    public static ItemDTO toItemDTO(Item item, Long genderId, Long itemImagesId, Long brandId) {
        ItemDTO itemDTO = new ItemDTO();
        itemDTO.setId(item.getId());
        itemDTO.setItemName(item.getItemName());
        itemDTO.setPrice(item.getPrice());
        itemDTO.setCreatedAt(item.getCreatedAt());
        itemDTO.setMemberId(item.getMemberId());
        itemDTO.setCategoryId(item.getCategoryId());
        itemDTO.setGenderId(genderId);
        itemDTO.setItemImagesId(itemImagesId);
        itemDTO.setBrandId(brandId);
        return itemDTO;
    }
}
