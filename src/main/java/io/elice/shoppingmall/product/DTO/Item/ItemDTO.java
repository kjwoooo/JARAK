package  io.elice.shoppingmall.product.DTO.Item;

import io.elice.shoppingmall.product.Entity.Item.Item;
import io.elice.shoppingmall.product.Entity.Item.ItemDetail;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ItemDTO {
    private Long id;
    private String itemName;
    private int price;
    private String gender;
}