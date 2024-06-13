package io.elice.shoppingmall.product.Entity.Item;

import io.elice.shoppingmall.product.DTO.Item.ItemDetailDTO;
import io.elice.shoppingmall.product.DTO.ReviewDTO;
import io.elice.shoppingmall.product.Entity.Review.Review;
import io.elice.shoppingmall.product.Repository.Item.ItemRepository;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ItemDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String color;
    private String size;
    private Integer quantity;

    @ManyToOne
    @JoinColumn(name= "item_id")
    private Item item;

    public static ItemDetail toSaveItemDetail(ItemDetailDTO itemDetailDTO, Item item){
        ItemDetail itemDetail = new ItemDetail();
        itemDetail.setId(itemDetail.getId());
        itemDetail.setColor(itemDetail.getColor());
        itemDetail.setSize(itemDetailDTO.getSize());
        itemDetail.setQuantity(itemDetailDTO.getQuantity());
        itemDetail.setItem(item);
        return itemDetail;
    }
}
