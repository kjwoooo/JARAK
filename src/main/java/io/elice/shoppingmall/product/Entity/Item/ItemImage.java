package io.elice.shoppingmall.product.Entity.Item;

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
public class ItemImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 2048)
    private String filePath;

    @Column
    private String fileName;

    @Column(nullable = true)
    private Boolean isMain;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;

}