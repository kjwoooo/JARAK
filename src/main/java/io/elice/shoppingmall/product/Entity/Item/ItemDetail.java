package io.elice.shoppingmall.product.Entity.Item;

import io.elice.shoppingmall.product.Entity.Option.Color;
import io.elice.shoppingmall.product.Entity.Option.Size;
import jakarta.persistence.*;
import lombok.*;

import java.util.Collection;

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
    private Collection<Color> colors;

    @OneToMany
    @JoinColumn(name = "size_id")
    private Collection<Size> sizes;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;
}