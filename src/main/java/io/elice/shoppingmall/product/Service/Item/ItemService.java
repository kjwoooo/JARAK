package io.elice.shoppingmall.product.Service.Item;

import io.elice.shoppingmall.product.S3.S3Uploader;
import io.elice.shoppingmall.category.entity.Category;
import io.elice.shoppingmall.category.repository.CategoryRepository;
import io.elice.shoppingmall.exception.CustomException;
import io.elice.shoppingmall.exception.ErrorCode;
import io.elice.shoppingmall.option.entity.Brand;
import io.elice.shoppingmall.option.repository.BrandRepository;
import io.elice.shoppingmall.product.DTO.Item.ItemDTO;
import io.elice.shoppingmall.product.DTO.Item.ItemDetailDTO;
import io.elice.shoppingmall.product.DTO.Item.ItemImageDTO;
import io.elice.shoppingmall.product.Entity.Item.Item;
import io.elice.shoppingmall.product.Entity.Item.ItemDetail;
import io.elice.shoppingmall.product.Entity.Item.ItemImage;
import io.elice.shoppingmall.product.Repository.Item.ItemDetailRepository;
import io.elice.shoppingmall.product.Repository.Item.ItemImageRepository;
import io.elice.shoppingmall.product.Repository.Item.ItemRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ItemService {
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private ItemDetailRepository itemDetailRepository;
    @Autowired
    private ItemImageRepository itemImageRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private BrandRepository brandRepository;
    @Autowired
    private S3Uploader s3Uploader;

    // DTO를 엔티티로 변환하는 함수
    private Item convertToEntity(ItemDTO itemDTO) {
        Item item = new Item();
        item.setId(itemDTO.getId());
        item.setItemName(itemDTO.getItemName());
        item.setPrice(itemDTO.getPrice());
        item.setGender(itemDTO.getGender());

        Brand brand = brandRepository.findById(itemDTO.getBrandId())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_BRAND));
        item.setBrand(brand);

        Category category = categoryRepository.findById(itemDTO.getCategoryId())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_CATEGORY));
        item.setCategory(category);

        List<ItemImage> itemImages= itemImageRepository.findAllByItemId(item.getId());
        item.setItemImages(itemImages);
        List<ItemDetail> itemDetails = itemDetailRepository.findAllByItemId(item.getId());
        item.setItemDetails(itemDetails);
        return item;
    }

    // 엔티티를 DTO로 변환하는 함수
    private ItemDTO convertToDTO(Item item) {
        List<ItemImage> itemImages = itemImageRepository.findAllByItemId(item.getId());
        List<ItemImageDTO> itemImageDTOs = itemImages.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        List<ItemDetail> itemDetails = itemDetailRepository.findAllByItemId(item.getId());
        List<ItemDetailDTO> itemDetailDTOs = itemDetails.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return ItemDTO.builder()
                .id(item.getId())
                .itemName(item.getItemName())
                .price(item.getPrice())
                .gender(item.getGender())
                .categoryId(item.getCategory().getId())
                .brandId(item.getBrand().getId())
                .itemImageDTOs(itemImageDTOs)
                .itemDetailDTOs(itemDetailDTOs)
                .build();
    }

    // ItemDetailDTO를 ItemDetail로 변환하는 함수
    private ItemDetail convertToEntity(ItemDetailDTO itemDetailDTO) {
        ItemDetail itemDetail = new ItemDetail();
        itemDetail.setId(itemDetailDTO.getId());
        itemDetail.setColor(itemDetailDTO.getColor());
        itemDetail.setSize(itemDetailDTO.getSize());
        itemDetail.setQuantity(itemDetailDTO.getQuantity());
        Item item = itemRepository.findById(itemDetailDTO.getItemId())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ITEM));
        itemDetail.setItem(item);
        return itemDetail;
    }

    // ItemDetail을 ItemDetailDTO로 변환하는 함수
    private ItemDetailDTO convertToDTO(ItemDetail itemDetail) {
        return ItemDetailDTO.builder()
                .id(itemDetail.getId())
                .color(itemDetail.getColor())
                .size(itemDetail.getSize())
                .quantity(itemDetail.getQuantity())
                .itemId(itemDetail.getItem().getId())
                .build();
    }

    private ItemImage converToEntity(ItemImageDTO itemImageDTO) {
        ItemImage itemImage = new ItemImage();
        itemImage.setId(itemImageDTO.getId());
        itemImage.setFileName(itemImageDTO.getFileName());
        itemImage.setFilePath(itemImageDTO.getFilePath());
        itemImage.setIsMain(itemImageDTO.getIsMain());
        Item item = itemRepository.findById(itemImageDTO.getItemId())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ITEM));
        itemImage.setItem(item);
        return itemImage;
    }

    private ItemImageDTO convertToDTO(ItemImage itemImage) {
        return ItemImageDTO.builder()
                .id(itemImage.getId())
                .filePath(itemImage.getFilePath())
                .fileName(itemImage.getFileName())
                .itemId(itemImage.getItem().getId())
                .isMain(itemImage.getIsMain())
                .build();
    }

    // 모든 상품 출력
    public List<ItemDTO> getAllItems() {
        List<Item> items = itemRepository.findAll();
        List<ItemDTO> itemDTOs = new ArrayList<>();

        for (Item item : items) {
            ItemDTO itemDTO = convertToDTO(item);
            itemDTOs.add(itemDTO);
        }

        return itemDTOs;
    }

    // Get Item by ID
    public ItemDTO getItemById(Long id) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ITEM));
        return convertToDTO(item);
    }

    @Transactional
    public ItemDTO createItem(ItemDTO itemDTO, MultipartFile mainFile, MultipartFile subFile) throws IOException {
        validateImageFile(mainFile);
        validateImageFile(subFile);

        Item savedItem = itemRepository.save(convertToEntity(itemDTO));
//        itemDTO.setId(savedItem.getId());

        List<String> mainFileInfo = s3Uploader.uploadFiles(mainFile, "itemImageDir");
        List<String> subFileInfo = s3Uploader.uploadFiles(subFile, "itemImageDir");

        ItemImage mainItemImage = new ItemImage();
        mainItemImage.setId(itemDTO.getId());
        mainItemImage.setFileName(mainFileInfo.get(0));
        mainItemImage.setFilePath(mainFileInfo.get(1));
        mainItemImage.setItem(savedItem);
        mainItemImage.setIsMain(true);
//        ItemImageDTO mainItemImageDTO = convertToDTO(itemImageRepository.save(mainItemImage));

        ItemImage subItemImage = new ItemImage();
        subItemImage.setId(itemDTO.getId());
        subItemImage.setFileName(subFileInfo.get(0));
        subItemImage.setFilePath(subFileInfo.get(1));
        subItemImage.setItem(savedItem);
        subItemImage.setIsMain(false);
//        ItemImageDTO subItemImageDTO = convertToDTO(itemImageRepository.save(subItemImage));

        List<ItemImage> itemImages = new ArrayList<>();
        itemImages.add(mainItemImage);
        itemImages.add(subItemImage);
        savedItem.setItemImages(itemImages);

        return convertToDTO(savedItem);
    }

//    @Transactional
//    public ItemDTO updateItem(Long itemId, ItemDTO itemDTO, MultipartFile mainFile, MultipartFile subFile) throws IOException {
//        validateImageFile(mainFile);
//        validateImageFile(subFile);
//
//        Item existingItem = itemRepository.findById(itemId)
//                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ITEM));
//        // ItemDTO의 내용으로 기존 아이템 업데이트
//        existingItem.setItemName(itemDTO.getItemName());
//        existingItem.setGender(itemDTO.getGender());
//        existingItem.setPrice(itemDTO.getPrice());
//        Brand brand = brandRepository.findById(itemDTO.getBrandId())
//                        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_BRAND));
//        existingItem.setBrand(brand);
//        Category category = categoryRepository.findById(itemDTO.getCategoryId())
//                        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_CATEGORY));
//        existingItem.setCategory(category);
//
//        // 기타 필요한 필드 업데이트
//        itemRepository.save(existingItem);
//        itemImageRepository.deleteAllByItemId(existingItem.getId());
//
//        List<String> mainFileInfo = s3Uploader.uploadFiles(mainFile, "itemImageDir");
//        List<String> subFileInfo = s3Uploader.uploadFiles(subFile, "itemImageDir");
//
//        ItemImage mainItemImage = new ItemImage();
//        mainItemImage.setId(itemDTO.getId());
//        mainItemImage.setFileName(mainFileInfo.get(0));
//        mainItemImage.setFilePath(mainFileInfo.get(1));
//        mainItemImage.setItem(existingItem);
//        mainItemImage.setIsMain(true);
//        ItemImageDTO mainItemImageDTO = convertToDTO(itemImageRepository.save(mainItemImage));
//
//        ItemImage subItemImage = new ItemImage();
//        subItemImage.setId(itemDTO.getId());
//        subItemImage.setFileName(subFileInfo.get(0));
//        subItemImage.setFilePath(subFileInfo.get(1));
//        subItemImage.setItem(existingItem);
//        subItemImage.setIsMain(false);
//        ItemImageDTO subItemImageDTO = convertToDTO(itemImageRepository.save(subItemImage));
//
//        List<ItemImageDTO> itemImageDTOs = new ArrayList<>();
//        itemImageDTOs.add(mainItemImageDTO);
//        itemImageDTOs.add(subItemImageDTO);
//
//        itemDTO.setItemImageDTOs(itemImageDTOs);
//        return itemDTO;
//    }

    @Transactional
    public ItemDTO updateItem(Long itemId, ItemDTO itemDTO, MultipartFile mainFile, MultipartFile subFile) throws IOException {
        if (mainFile != null) {
            validateImageFile(mainFile);
        }
        if (subFile != null) {
            validateImageFile(subFile);
        }
        List<ItemImage> itemImages = new ArrayList<>();

        Item existingItem = itemRepository.findById(itemId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ITEM));

        // ItemDTO의 내용으로 기존 아이템 업데이트
        existingItem.setItemName(itemDTO.getItemName());
        existingItem.setGender(itemDTO.getGender());
        existingItem.setPrice(itemDTO.getPrice());

        Brand brand = brandRepository.findById(itemDTO.getBrandId())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_BRAND));
        existingItem.setBrand(brand);
        Category category = categoryRepository.findById(itemDTO.getCategoryId())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_CATEGORY));
        existingItem.setCategory(category);

        if(mainFile!=null){
            List<String> mainFileInfo = s3Uploader.uploadFiles(mainFile, "itemImageDir");
            ItemImage mainItemImage = new ItemImage();
            mainItemImage.setFileName(mainFileInfo.get(0));
            mainItemImage.setFilePath(mainFileInfo.get(1));
            mainItemImage.setItem(existingItem);
            mainItemImage.setIsMain(true);
            itemImageRepository.deleteByItemIdAndIsMain(existingItem.getId(), true);
            itemImages.add(itemImageRepository.save(mainItemImage));
        }else{
            itemImages.add(itemImageRepository.findByItemIdAndIsMain(existingItem.getId(),true));
        }

        if(subFile!=null){
            List<String> subFileInfo = s3Uploader.uploadFiles(subFile, "itemImageDir");
            ItemImage subItemImage = new ItemImage();
            subItemImage.setFileName(subFileInfo.get(0));
            subItemImage.setFilePath(subFileInfo.get(1));
            subItemImage.setItem(existingItem);
            subItemImage.setIsMain(false);
            itemImageRepository.deleteByItemIdAndIsMain(existingItem.getId(), false);
            itemImages.add(itemImageRepository.save(subItemImage));
        }else{
           itemImages.add(itemImageRepository.findByItemIdAndIsMain(existingItem.getId(), false));
        }
        // 기타 필요한 필드 업데이트
        existingItem.setItemImages(itemImages);
        itemRepository.save(existingItem);

        return convertToDTO(existingItem);
    }

    // Delete Item
    public void deleteItem(Long id) {
        Optional<Item> optionalItem = itemRepository.findById(id);
        if (optionalItem.isPresent()) {
            Item item = optionalItem.get();

            // 파일 삭제
            List<ItemImage> itemImages = itemImageRepository.findAllByItemId(id);
            for (ItemImage itemImage : itemImages) {
                File file = new File(itemImage.getFilePath());
                if (file.exists()) {
                    file.delete();
                }
                itemImageRepository.delete(itemImage);
            }

            itemRepository.deleteById(id);
        }
    }

    // Create ItemDetail
    public ItemDetailDTO createItemDetail(ItemDetailDTO itemDetailDTO) {
        ItemDetail itemDetail = convertToEntity(itemDetailDTO);
        itemDetail = itemDetailRepository.save(itemDetail);
        return convertToDTO(itemDetail);
    }


    // Get All ItemDetails
    public List<ItemDetailDTO> getAllItemDetails(Long id) {
        List<ItemDetail> itemDetails = itemDetailRepository.findByItemId(id);
        List<ItemDetailDTO> itemDetailDTOs = new ArrayList<>();

        for (ItemDetail itemDetail : itemDetails) {
            ItemDetailDTO itemDetailDTO = convertToDTO(itemDetail);
            itemDetailDTOs.add(itemDetailDTO);
        }

        return itemDetailDTOs;
    }

    // Get ItemDetail by ID
    public ItemDetailDTO getItemDetailById(Long id) {
        ItemDetail itemDetail = itemDetailRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ITEM_DETAIL));
        return convertToDTO(itemDetail);
    }

    public ItemImageDTO getImageById(Long id){
        ItemImage itemImage = itemImageRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ITEM_IMAGE));
        return convertToDTO(itemImage);
    }


    // Update ItemDetail
    @Transactional
    public ItemDetailDTO updateItemDetail(Long id, ItemDetailDTO itemDetailDTO) {

        ItemDetail itemDetail = itemDetailRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ITEM_DETAIL));

        itemDetail.setColor(itemDetailDTO.getColor());
        itemDetail.setSize(itemDetailDTO.getSize());
        itemDetail.setQuantity(itemDetailDTO.getQuantity());

        ItemDetail updatedItemDetail = itemDetailRepository.save(itemDetail);
        ItemDetailDTO updatedItemDetailDTO = convertToDTO(updatedItemDetail);

        return updatedItemDetailDTO;
    }

    // Delete ItemDetail
    @Transactional
    public void deleteItemDetail(Long id) {
        if (!itemDetailRepository.existsById(id)) {
            throw new CustomException(ErrorCode.NOT_FOUND_ITEM_DETAIL);
        }
        itemDetailRepository.deleteById(id);
    }

    //find all itemimages
    public List<ItemImageDTO> getAllItemImages(Long id) {
        List<ItemImage> itemImages = itemImageRepository.findAllByItemId(id);
        List<ItemImageDTO> itemImageDTOs = new ArrayList<>();

        for (ItemImage itemImage : itemImages) {
            ItemImageDTO itemImageDTO = convertToDTO(itemImage);
            itemImageDTOs.add(itemImageDTO);
        }

        return itemImageDTOs;
    }

    //find itemimage by Id
    public ItemImageDTO getItemImageById(Long id) {
        ItemImage itemImage = itemImageRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ITEM_IMAGE));
        return convertToDTO(itemImage);
    }

    @Transactional
    public void deleteItemImage(Long id) {
        if (!itemImageRepository.existsById(id)) {
            throw new CustomException(ErrorCode.NOT_FOUND_ITEM_IMAGE);
        }
        itemImageRepository.deleteById(id);
    }

    @Transactional
    public void reduceQuantity(Long itemId, String color, String size, Integer quantity) {
        ItemDetail itemDetail = itemDetailRepository.findByitemIdAndColorAndSize(itemId, color, size);
        if (itemDetail.getQuantity() < quantity) {
            throw new CustomException(ErrorCode.INVALID_ITEM_QUANTITY);
        }
        itemDetail.setQuantity(itemDetail.getQuantity() - quantity);
    }

    // 이미지 파일 유효성 검사 메서드
    private void validateImageFile(MultipartFile file) {
        if (file == null) {
            return;  // 파일이 null인 경우 그냥 반환
        }
        // 기존의 파일 검증 로직
        if (!file.getContentType().startsWith("image/")) {
            throw new CustomException(ErrorCode.INVALID_IMAGE_FILE);
        }
    }
}