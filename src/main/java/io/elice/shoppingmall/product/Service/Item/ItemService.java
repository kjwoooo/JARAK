package io.elice.shoppingmall.product.Service.Item;

import io.elice.shoppingmall.exception.CustomException;
import io.elice.shoppingmall.exception.ErrorCode;
import io.elice.shoppingmall.product.DTO.Item.ItemDTO;
import io.elice.shoppingmall.product.DTO.Item.ItemDetailDTO;
import io.elice.shoppingmall.product.Entity.Item.Item;
import io.elice.shoppingmall.product.Entity.Item.ItemDetail;
import io.elice.shoppingmall.product.Entity.Item.ItemImages;
import io.elice.shoppingmall.option.entity.Gender;
import io.elice.shoppingmall.option.entity.Brand;
import io.elice.shoppingmall.product.Repository.Item.ItemDetailRepository;
import io.elice.shoppingmall.product.Repository.Item.ItemImagesRepository;
import io.elice.shoppingmall.product.Repository.Item.ItemRepository;
import jakarta.mail.Multipart;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ItemService { //dev branch 가져와서 category, member 엔티티 추가적으로 수정해야함

    private final ItemRepository itemRepository;
    private final ItemImagesRepository itemImagesRepository;
    private final ItemDetailRepository itemDetailRepository;

    private ItemImages itemImages;
    private Brand brand;
    private Gender gender;


    //item 생성
    @Transactional
    public void save(ItemDTO itemDTO) throws IOException {
//        Item item = Item.toSaveItem(itemDTO, itemImages, brand, gender);
//        itemRepository.save(item);
        Item itemEntity = Item.toSaveItem(itemDTO, itemImages, brand, gender); //id값없음
        Long savedId = itemRepository.save(itemEntity).getId();
        Item item = itemRepository.findById(savedId).get(); //id값을 위해 다시 entity 얻어옴
        for(MultipartFile imageFile : itemDTO.getItemImages()){
            String originalFilename = imageFile.getOriginalFilename();
            String storedFileName = System.currentTimeMillis() + "_" + originalFilename;
            String savePath = "C:/eliceshoppingmall_img/" + storedFileName;
            imageFile.transferTo(new File(savePath));
            ItemImages itemImages = ItemImages.toItemImages(item, originalFilename, storedFileName);
            itemImagesRepository.save(itemImages);
        }
    }

    //ItemDetail 생성
    @Transactional
    public ItemDetail saveItemDetail(ItemDetail itemDetail) {
        return itemDetailRepository.save(itemDetail);
    }

    //전체 상품목록 조회
    @Transactional
    public List<ItemDTO> findAll(){
        List<Item> itemList = itemRepository.findAll();
        List<ItemDTO> itemDTOList = new ArrayList<>();
        for(Item item : itemList){
            itemDTOList.add(ItemDTO.toItemDTO(item, itemImages.getId(), brand.getId(), gender.getId()));
        }
        return itemDTOList;
    }

    //한 상품의 모든 ItemDetail조회
    public List<ItemDetailDTO> findItemDetailsByItemId(Long itemId) {
        Item item = itemRepository.findById(itemId).orElseThrow(()->
            new CustomException(ErrorCode.NOT_FOUND_ITEM));

        List<ItemDetail> itemDetailList = itemDetailRepository.findByItem(item);
        List<ItemDetailDTO> itemDetailDTOList = new ArrayList<>();
        for(ItemDetail itemDetail: itemDetailList){
            itemDetailDTOList.add(ItemDetailDTO.toItemDetailDTO(itemDetail, itemId));
        }
        return itemDetailDTOList;
    }

    //특정 상품 조회
    @Transactional
    public ItemDTO findById(Long id){
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
    public void delete(Long id){
        itemRepository.deleteById(id);
    }
}
