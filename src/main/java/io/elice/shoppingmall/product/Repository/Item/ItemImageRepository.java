package io.elice.shoppingmall.product.Repository.Item;

import io.elice.shoppingmall.exception.CustomException;
import io.elice.shoppingmall.product.Entity.Item.ItemImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemImageRepository extends JpaRepository<ItemImage, Long> {

    List<ItemImage> findAllByItemId(Long id);

//    @Query("SELECT ii FROM ItemImage ii WHERE ii.item.id = :itemId AND ii.isMain = false")
//    ItemImage findByItemIdAndIsMainFalse(@Param("itemId") Long itemId);

    ItemImage findByItemIdAndIsMain(Long id, boolean b);

    void deleteByItemIdAndIsMain(Long id, boolean b);
}
