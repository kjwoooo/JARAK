package io.elice.shoppingmall.product.Service;

import io.elice.shoppingmall.product.DTO.ItemDTO;
import io.elice.shoppingmall.product.Entity.Item.Item;
import io.elice.shoppingmall.product.Entity.Item.ItemImages;
import io.elice.shoppingmall.product.Entity.Option.Brand;
import io.elice.shoppingmall.product.Entity.Option.Gender;
import io.elice.shoppingmall.product.Repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ItemService { //dev branch 가져와서 category, member 엔티티 추가적으로 수정해야함

    private final ItemRepository itemRepository;
    private ItemImages itemImages;
    private Brand brand;
    private Gender gender;


    //item 생성
    public void save(ItemDTO itemDTO){
        Item item = Item.toSaveItem(itemDTO, itemImages, brand, gender);
        itemRepository.save(item);
    }

    //전체 상품목록 조회
    public List<ItemDTO> findAll(){
        List<Item> itemList = itemRepository.findAll();
        List<ItemDTO> itemDTOList = new ArrayList<>();
        for(Item item : itemList){
            itemDTOList.add(ItemDTO.toItemDTO(item, itemImages.getId(), brand.getId(), gender.getId()));
        }
        return itemDTOList;
    }

    //특정 상품 조회
    public ItemDTO findById(Integer id){
        Optional<Item> optionalItem =  itemRepository.findById(id);
        if(optionalItem.isPresent()){
            Item item = optionalItem.get();
            ItemDTO itemDTO = ItemDTO.toItemDTO(item, itemImages.getId(), brand.getId(), gender.getId());
            return itemDTO;
        }
        else{
            return null;
        }
    }

    //상품 수정
    public ItemDTO update(ItemDTO itemDTO){
        Item item = Item.toUpdateItem(itemDTO, itemImages, brand, gender);
        itemRepository.save(item);
        return findById(itemDTO.getId());
    }

    //상품 삭제
    public void delete(Integer id){
        itemRepository.deleteById(id);
    }
}
