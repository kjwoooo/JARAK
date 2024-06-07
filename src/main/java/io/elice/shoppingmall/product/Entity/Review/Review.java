package io.elice.shoppingmall.product.Entity.Review;

import io.elice.shoppingmall.entity.base.BaseEntity;
import io.elice.shoppingmall.product.Entity.Item.Item;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Review extends BaseEntity { //member entity 받으면 연관관계 매핑 마무리
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String memberId;

    @Column(nullable = false, length = 20)
    private String title;

    @Column(nullable = false, length = 200)
    private String content;

    @Column
    private String img;

    @Column
    private double rate;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;
}
