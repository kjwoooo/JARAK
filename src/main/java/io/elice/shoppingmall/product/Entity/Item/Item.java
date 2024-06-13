package io.elice.shoppingmall.product.Entity.Item;


//import io.elice.shoppingmall.category.controller.CategoryController;
import io.elice.shoppingmall.entity.baseEntity.BaseEntity;
import io.elice.shoppingmall.product.DTO.Item.ItemDTO;
//import io.elice.shoppingmall.product.Entity.Option.brand;
import io.elice.shoppingmall.option.entity.Gender;
import io.elice.shoppingmall.option.entity.Brand;
import io.elice.shoppingmall.product.DTO.Item.ItemImagesDTO;
import io.elice.shoppingmall.product.Entity.Review.Request;
import io.elice.shoppingmall.product.Entity.Review.Review;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Item extends BaseEntity {//    Member,Category entity 받으면 연관관계 매핑 마무리
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Long memberId;

    @Column
    private Long categoryId;

    @Column(nullable = false)
    private String itemName;

    @Column(nullable = false)
    private Integer price;

    @OneToMany(mappedBy = "item", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<ItemImages> itemImagesList = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "brand_id")
    private Brand brand;

    @ManyToOne
    @JoinColumn(name = "gender_id")
    private Gender gender;


    @OneToMany(mappedBy = "item")
    private List<Request> requests = new ArrayList<>();

    @OneToMany(mappedBy = "item")
    private List<Review> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemDetail> itemDetails;

    public static Item toSaveItem(ItemDTO itemDTO,ItemImages itemImages, Brand brand, Gender gender){
        Item item = new Item();
        item.setItemName(itemDTO.getItemName());
        item.setPrice(itemDTO.getPrice());
//        item.setItemImages(itemImages);
        item.setBrand(brand);
        item.setGender(gender);
        return item;
    }

    public static Item toUpdateItem(ItemDTO itemDTO, ItemImages itemImages, Brand brand, Gender gender){
        Item item = new Item();
        item.setId(itemDTO.getId());
        item.setItemName(itemDTO.getItemName());
        item.setPrice(itemDTO.getPrice());
//        item.setItemImages(itemImages);
        item.setBrand(brand);
        item.setGender(gender);
        return item;
    }

}