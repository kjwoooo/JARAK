package io.elice.shoppingmall.product.Entity.Item;

import io.elice.shoppingmall.option.entity.Color;
import io.elice.shoppingmall.option.entity.Size;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ItemDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer quantity;

    @OneToMany
    @JoinColumn(name = "color_id")
    private List<Color> colors;

    @OneToMany
    @JoinColumn(name = "size_id")
    private List<Size> sizes;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;
}