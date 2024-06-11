package io.elice.shoppingmall.product.DTO.Item;

import io.elice.shoppingmall.product.Entity.Item.ItemImages;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ItemImagesDTO {
    private Long id;
    private String mainImage;
    private String detailImage;

    public static ItemImagesDTO toItemImagesDTO(ItemImages itemImages){
        ItemImagesDTO itemImagesDTO = new ItemImagesDTO();
        itemImagesDTO.setId(itemImages.getId());
        itemImagesDTO.setMainImage(itemImagesDTO.getMainImage());
        itemImagesDTO.setDetailImage(itemImagesDTO.getDetailImage());
        return itemImagesDTO;
    }
}
