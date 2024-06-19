package io.elice.shoppingmall.product.Entity.Review;


import io.elice.shoppingmall.member.entity.Member;
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
public class Review extends BaseEntity { //member entity 받으면 연관관계 매핑 마무리

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 20)
    private String title;

    @Column(nullable = false, length = 200)
    private String content;

    @Column
    private double rate;

    @Column
    private String username;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;
}
