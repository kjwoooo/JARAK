package  io.elice.shoppingmall.product.DTO.Item;

import io.elice.shoppingmall.product.Entity.Item.Item;
import io.elice.shoppingmall.product.Entity.Item.ItemDetail;
import jdk.jshell.Snippet;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemDetailDTO {
    private Long id;
    private String color;
    private String size;
    private int quantity;
    private Long itemId;
}