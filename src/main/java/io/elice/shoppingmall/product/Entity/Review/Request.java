package io.elice.shoppingmall.product.Entity.Review;

import io.elice.shoppingmall.entity.baseEntity.BaseEntity;
import io.elice.shoppingmall.product.Entity.Item.Item;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Request extends BaseEntity { //Member Entity 받으면 연관관계 매핑 마무리
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String memberId;

    @Column(nullable = false)
    private String title;

    @Column
    private String content;

    @Column
    private String img;

    @Column
    private String comment;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;

}
