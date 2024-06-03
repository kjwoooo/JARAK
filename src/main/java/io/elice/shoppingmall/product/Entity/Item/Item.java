package io.elice.shoppingmall.product.Entity.Item;

import io.elice.shoppingmall.entity.baseEntity.BaseEntity;
import io.elice.shoppingmall.product.Entity.Option.Brand;
import io.elice.shoppingmall.product.Entity.Option.Gender;
import io.elice.shoppingmall.product.Entity.Review.Request;
import io.elice.shoppingmall.product.Entity.Review.Review;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Collection;


@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Item extends BaseEntity {//    Member,Category entity 받으면 연관관계 매핑 마무리
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String memberId;

    @Column
    private String categoryId;

    @Column(nullable = false)
    private String itemName;

    @Column(nullable = false)
    private Integer price;

    @OneToOne
    @JoinColumn(name = "item_images_id")
    private ItemImages itemImages;

    @ManyToOne
    @JoinColumn(name = "brand_id")
    private Brand brand;

    @ManyToOne
    @JoinColumn(name = "gender_id")
    private Gender gender;

    @OneToMany(mappedBy = "item")
    private Collection<ItemDetail> itemDetails = new ArrayList<>();

    @OneToMany(mappedBy = "item")
    private Collection<Request> requests = new ArrayList<>();

    @OneToMany(mappedBy = "item")
    private Collection<Review> reviews = new ArrayList<>();
}