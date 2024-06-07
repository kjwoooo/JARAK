package io.elice.shoppingmall.product.Repository;

import io.elice.shoppingmall.product.Entity.Item.ItemImages;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemImagesRepository extends JpaRepository<ItemImages, Integer> {
}
