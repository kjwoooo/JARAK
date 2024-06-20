package io.elice.shoppingmall.cart.domain.cartItems.Entity;

import io.elice.shoppingmall.cart.domain.cart.Entity.Cart;
import io.elice.shoppingmall.cart.domain.cartItems.DTO.CartItemAddResponseDto;
import io.elice.shoppingmall.entity.baseEntity.BaseEntity;

import io.elice.shoppingmall.product.Entity.Item.Item;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "cartitems")
public class CartItems extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    //item:CartItem = 1:N 양방향, 연관관계의 주인은 cartitem
    @ManyToOne
    @JoinColumn(name="item_id")
    private Item item_id;

    @ManyToOne
    @JoinColumn(name="cart_id")
    private Cart cart_id;

    @Column(nullable = true)
    private boolean selected;

    private int quantity;

    private String color;

    private String size;

    public CartItemAddResponseDto toCartItemAddResponseDto() {
        return new CartItemAddResponseDto(id, quantity, getCreatedAt());
    }
}
