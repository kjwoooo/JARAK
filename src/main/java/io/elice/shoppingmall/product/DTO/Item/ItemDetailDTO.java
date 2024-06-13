package io.elice.shoppingmall.product.DTO.Item;


import io.elice.shoppingmall.product.Entity.Item.ItemDetail;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemDetailDTO {
    private Long id;
    private String color;
    private String size;
    private Integer quantity;
    private Long itemId;

    public static ItemDetailDTO toItemDetailDTO(ItemDetail itemDetail, Long itemId){
        ItemDetailDTO itemDetailDTO = new ItemDetailDTO();
        itemDetailDTO.setId(itemDetail.getId());
        itemDetailDTO.setColor(itemDetail.getColor());
        itemDetailDTO.setSize(itemDetail.getSize());
        itemDetailDTO.setQuantity(itemDetail.getQuantity());
        return itemDetailDTO;
    }

}
