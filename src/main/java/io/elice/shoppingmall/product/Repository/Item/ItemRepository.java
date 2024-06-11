package io.elice.shoppingmall.product.Repository.Item;

import io.elice.shoppingmall.product.Entity.Item.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

}