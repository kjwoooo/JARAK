package io.elice.shoppingmall.product.Service.Item;

import io.elice.shoppingmall.product.DTO.Item.ItemImagesDTO;
import io.elice.shoppingmall.product.Entity.Item.ItemImages;
import io.elice.shoppingmall.product.Repository.Item.ItemImagesRepository;
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

    public void delete(Long id) {
        itemImagesRepository.deleteById(id);
    }
}
