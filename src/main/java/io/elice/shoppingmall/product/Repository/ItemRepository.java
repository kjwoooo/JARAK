package io.elice.shoppingmall.product.Repository;

import io.elice.shoppingmall.product.Entity.Item.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Integer> {

}