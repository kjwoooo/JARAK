package io.elice.shoppingmall.product.Repository.Item;

import io.elice.shoppingmall.product.Entity.Item.ItemDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemDetailRepository  extends JpaRepository<ItemDetail, Long> {
    List<ItemDetail> findByItemId(Long itemId);
}
