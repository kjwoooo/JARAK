package io.elice.shoppingmall.product.Entity.Item;

import io.elice.shoppingmall.product.DTO.ItemDTO;
import io.elice.shoppingmall.product.DTO.ItemImagesDTO;
import io.elice.shoppingmall.product.Entity.Option.Brand;
import io.elice.shoppingmall.product.Entity.Option.Gender;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ItemImages {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String mainImage;

    @Column
    private String detailImage;

    public static ItemImages toSaveItem(ItemImagesDTO itemImagesDTO){
        ItemImages itemImages = new ItemImages();
        itemImages.setMainImage(itemImages.getMainImage());
        itemImages.setDetailImage(itemImages.getDetailImage());
        return itemImages;
    }
}
