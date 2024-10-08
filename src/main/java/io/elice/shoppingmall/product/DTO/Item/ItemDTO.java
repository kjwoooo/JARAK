package  io.elice.shoppingmall.product.DTO.Item;

import io.elice.shoppingmall.product.Entity.Item.Item;
import io.elice.shoppingmall.product.Entity.Item.ItemDetail;
import io.elice.shoppingmall.product.Entity.Item.ItemImage;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemDTO {
    private Long id;
    private String itemName;
    private int price;
    private String gender;
    private Long brandId;
    private Long categoryId;
    private List<ItemImageDTO> itemImageDTOs;
    private List<ItemDetailDTO> itemDetailDTOs;
}