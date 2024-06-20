package io.elice.shoppingmall.product.Entity.Review;


import io.elice.shoppingmall.member.entity.Member;
import io.elice.shoppingmall.entity.baseEntity.BaseEntity;
import io.elice.shoppingmall.product.Entity.Item.Item;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Request extends BaseEntity { //Member Entity 받으면 연관관계 매핑 마무리
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column
    private String content;

    @Column
    private String username;

    @OneToMany(mappedBy = "request", cascade = CascadeType.ALL)
    private List<Reply> replies;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;
}
