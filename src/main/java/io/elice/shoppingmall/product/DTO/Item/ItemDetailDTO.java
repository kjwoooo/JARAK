package  io.elice.shoppingmall.product.DTO.Item;

import io.elice.shoppingmall.product.Entity.Item.Item;
import io.elice.shoppingmall.product.Entity.Item.ItemDetail;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ItemDetailDTO {
    private Long id;
    private String color;
    private String size;
    private int quantity;
    private Long itemId;
}