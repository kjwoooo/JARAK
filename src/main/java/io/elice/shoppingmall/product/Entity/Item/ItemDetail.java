package io.elice.shoppingmall.product.Entity.Item;

import io.elice.shoppingmall.product.DTO.Item.ItemDetailDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ItemDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String color;
    private String size;
    private int quantity;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;
}