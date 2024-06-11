package io.elice.shoppingmall.product.Entity.Item;

import io.elice.shoppingmall.entity.baseEntity.BaseEntity;
import io.elice.shoppingmall.product.DTO.Item.ItemImagesDTO;
import jakarta.persistence.*;
import lombok.*;

/**
 erd변경 필요
 */
@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ItemImages extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String originalFileName;

    @Column
    private String storedFileName;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;

    public static ItemImages toItemImages(Item item, String originalFileName, String storedFileName){
        ItemImages itemImages = new ItemImages();
        itemImages.setOriginalFileName(originalFileName);
        itemImages.setStoredFileName(storedFileName);
        itemImages.setItem(item);
        return itemImages;
    }



    public static ItemImages toSaveItem(ItemImagesDTO itemImagesDTO){
        ItemImages itemImages = new ItemImages();

        return itemImages;
    }
}
