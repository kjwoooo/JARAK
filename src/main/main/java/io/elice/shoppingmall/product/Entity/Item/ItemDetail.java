package io.elice.shoppingmall.product.Entity.Item;

import io.elice.shoppingmall.entity.baseEntity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
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