package io.elice.shoppingmall.product.Service;

import io.elice.shoppingmall.product.DTO.ItemDTO;
import io.elice.shoppingmall.product.DTO.ItemImagesDTO;
import io.elice.shoppingmall.product.Entity.Item.Item;
import io.elice.shoppingmall.product.Entity.Item.ItemImages;
import io.elice.shoppingmall.product.Repository.ItemImagesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ItemImagesService {
    private final ItemImagesRepository itemImagesRepository;

    public void save(ItemImagesDTO itemImagesDTO){
        ItemImages itemImages = ItemImages.toSaveItem(itemImagesDTO);
        itemImagesRepository.save(itemImages);
    }

    public void delete(Integer id) {
        itemImagesRepository.deleteById(id);
    }
}
