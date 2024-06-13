package io.elice.shoppingmall.product.Repository.Item;

import io.elice.shoppingmall.product.Entity.Item.ItemImages;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemImagesRepository extends JpaRepository<ItemImages, Long> {
}
