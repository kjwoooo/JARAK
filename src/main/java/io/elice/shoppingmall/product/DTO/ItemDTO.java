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
    private Integer id;
    private Integer memberId;
    private Integer categoryId;
    private Integer genderId;
    private Integer itemImagesId;
    private Integer brandId;
    private String itemName;
    private Integer price;
    private LocalDateTime createdAt;

    public static ItemDTO toItemDTO(Item item, Integer memberId, Integer categoryId, Integer genderId, Integer itemImagesId, Integer brandId) {
        ItemDTO itemDTO = new ItemDTO();
        itemDTO.setId(item.getId());
        itemDTO.setItemName(item.getItemName());
        itemDTO.setPrice(item.getPrice());
        itemDTO.setCreatedAt(item.getCreatedAt());
        itemDTO.setMemberId(memberId);
        itemDTO.setCategoryId(categoryId);
        itemDTO.setGenderId(genderId);
        itemDTO.setItemImagesId(itemImagesId);
        itemDTO.setBrandId(brandId);
        return itemDTO;
    }
}
