package io.elice.shoppingmall.product.Service.Item;

import io.elice.shoppingmall.category.entity.Category;
import io.elice.shoppingmall.category.repository.CategoryRepository;
import io.elice.shoppingmall.product.DTO.Item.ItemDTO;
import io.elice.shoppingmall.product.DTO.Item.ItemDetailDTO;
import io.elice.shoppingmall.product.DTO.Item.ItemImageDTO;
import io.elice.shoppingmall.product.Entity.Item.Item;
import io.elice.shoppingmall.product.Entity.Item.ItemDetail;
import io.elice.shoppingmall.product.Entity.Item.ItemImage;
import io.elice.shoppingmall.product.Repository.Item.ItemDetailRepository;
import io.elice.shoppingmall.product.Repository.Item.ItemImageRepository;
import io.elice.shoppingmall.product.Repository.Item.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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

    // DTO를 엔티티로 변환하는 함수

    private Item convertToEntity(ItemDTO itemDTO) {
        Item item = new Item();
        item.setId(itemDTO.getId());
        item.setItemName(itemDTO.getItemName());
        item.setPrice(itemDTO.getPrice());
        item.setGender(itemDTO.getGender());
        Optional<Category> optionalCategory = categoryRepository.findById(itemDTO.getCategoryId());
        optionalCategory.ifPresent(item::setCategory);
        List<ItemImage> itemImages= itemImageRepository.findAllByItemId(item.getId());
        item.setItemImages(itemImages);
        return item;
    }

    // 엔티티를 DTO로 변환하는 함수
    private ItemDTO convertToDTO(Item item) {

        List<ItemImage> itemImages = itemImageRepository.findAllByItemId(item.getId());
        List<ItemImageDTO> itemImageDTOs = new ArrayList<>();
        for(ItemImage itemImage : itemImages){
            ItemImageDTO itemImageDTO = convertToDTO(itemImage);
            itemImageDTOs.add(itemImageDTO);
        }

        return ItemDTO.builder()
                .id(item.getId())
                .itemName(item.getItemName())
                .price(item.getPrice())
                .gender(item.getGender())
                .categoryId(item.getCategory().getId())
                .itemImageDTOs(itemImageDTOs)
                .build();
    }

    // ItemDetailDTO를 ItemDetail로 변환하는 함수
    private ItemDetail convertToEntity(ItemDetailDTO itemDetailDTO) {
        ItemDetail itemDetail = new ItemDetail();
        itemDetail.setId(itemDetailDTO.getId());
        itemDetail.setColor(itemDetailDTO.getColor());
        itemDetail.setSize(itemDetailDTO.getSize());
        itemDetail.setQuantity(itemDetailDTO.getQuantity());
        Optional<Item> item = itemRepository.findById(itemDetailDTO.getItemId());
        item.ifPresent(itemDetail::setItem);
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
        Optional<Item> item = itemRepository.findById(itemImageDTO.getItemId());
        item.ifPresent(itemImage::setItem);
        return itemImage;
    }

    private ItemImageDTO convertToDTO(ItemImage itemImage) {
        return ItemImageDTO.builder()
                .id(itemImage.getId())
                .filePath(itemImage.getFilePath())
                .fileName(itemImage.getFileName())
                .itemId(itemImage.getItem().getId())
                .build();
    }

//    public ItemDTO createItem(ItemDTO itemDTO, List<MultipartFile> files) throws IOException {
//        Item item = convertToEntity(itemDTO);
//        item = itemRepository.save(item);
//        itemDTO.setId(item.getId());
//        List<ItemImageDTO> itemImageDTOs = new ArrayList<>();
//
//        if (files != null && !files.isEmpty()) {
//            List<ItemImage> itemImages = new ArrayList<>();
//            for (MultipartFile file : files) {
//                if (file != null && !file.isEmpty()) {
//                    // 파일 저장 경로 설정
//                    String uploadDir = System.getProperty("user.dir") + "//src//main//resources//imagefiles";
//                    File uploadDirFile = new File(uploadDir);
//
//                    // UUID를 사용한 파일 이름 생성
//                    String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
//                    String filePath = uploadDir + File.separator + fileName;
//
//
//                    // 파일 저장
//                    File dest = new File(filePath);
//                    file.transferTo(dest);
//
//                    // 파일 경로와 이름 설정
//                    ItemImage itemImage = new ItemImage();
//                    itemImage.setFilePath(filePath);
//                    itemImage.setFileName(fileName);
//                    itemImage.setItem(item);
//
//                    itemImages.add(itemImage);
//                    itemImageRepository.save(itemImage);
//                    itemImageDTOs.add(convertToDTO(itemImage));
//                }
//            }
////            itemImageRepository.saveAll(itemImages);
//        }
//        itemDTO.setItemImageDTOs(itemImageDTOs);
//        return itemDTO;
//    }

    public ItemDTO createItem(ItemDTO itemDTO, List<MultipartFile> files) throws IOException {
        Item item = convertToEntity(itemDTO);
        item = itemRepository.save(item);
        itemDTO.setId(item.getId());

        List<ItemImageDTO> itemImageDTOs = processFilesAndSaveImages(item, files);
        itemDTO.setItemImageDTOs(itemImageDTOs);

        return itemDTO;
    }

    // READ: 모든 상품 출력
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
        Optional<Item> optionalItem = itemRepository.findById(id);
        if (optionalItem.isPresent()) {
            Item item = optionalItem.get();
            return convertToDTO(item);
        }
        return null; // Or throw an exception
    }


//    public ItemDTO updateItem(Long id, ItemDTO itemDTO, List<MultipartFile> files, List<Long> imageIdsToDelete) throws IOException {
//        Item existingItem = itemRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Item not found"));
//        existingItem.setItemName(itemDTO.getItemName());
//        existingItem.setPrice(itemDTO.getPrice());
//        existingItem.setGender(itemDTO.getGender());
//
//        // 이미지 삭제 처리
//        if (imageIdsToDelete != null && !imageIdsToDelete.isEmpty()) {
//            for (Long imageId : imageIdsToDelete) {
//                Optional<ItemImage> imageOpt = itemImageRepository.findById(imageId);
//                if (imageOpt.isPresent()) {
//                    ItemImage image = imageOpt.get();
//                    File file = new File(image.getFilePath());
//                    if (file.exists()) {
//                        file.delete();
//                    }
//                    itemImageRepository.delete(image);
//                }
//            }
//        }
//
//        // 새 파일 저장 처리
//        if (files != null && !files.isEmpty()) {
//            List<ItemImage> itemImages = new ArrayList<>();
//            for (MultipartFile file : files) {
//                if (file != null && !file.isEmpty()) {
//                    String uploadDir = System.getProperty("user.dir") + "//src//main//resources//imagefiles";
//                    File uploadDirFile = new File(uploadDir);
//
//                    String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
//                    String filePath = uploadDir + File.separator + fileName;
//
//                    File dest = new File(filePath);
//                    file.transferTo(dest);
//
//                    ItemImage itemImage = new ItemImage();
//                    itemImage.setFilePath(filePath);
//                    itemImage.setFileName(fileName);
//                    itemImage.setItem(existingItem);
//
//                    itemImages.add(itemImage);
//                }
//            }
//            itemImageRepository.saveAll(itemImages);
//        }
//
//        itemRepository.save(existingItem);
//        return convertToDTO(existingItem);
//    }

    public ItemDTO updateItem(Long itemId, ItemDTO itemDTO, List<MultipartFile> files) throws IOException {
        // 기존 아이템을 데이터베이스에서 조회
        Item existingItem = itemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item not found"));

        // ItemDTO의 내용으로 기존 아이템 업데이트
        existingItem.setItemName(itemDTO.getItemName());
        existingItem.setGender(itemDTO.getGender());
        existingItem.setPrice(itemDTO.getPrice());
        // 기타 필요한 필드 업데이트
        itemRepository.save(existingItem);

        List<ItemImageDTO> itemImageDTOs = processFilesAndSaveImages(existingItem, files);
        itemDTO.setId(existingItem.getId());
        itemDTO.setItemImageDTOs(itemImageDTOs);

        return itemDTO;
    }

    private List<ItemImageDTO> processFilesAndSaveImages(Item item, List<MultipartFile> files) throws IOException {
        List<ItemImageDTO> itemImageDTOs = new ArrayList<>();

        if (files != null && !files.isEmpty()) {
            List<ItemImage> itemImages = new ArrayList<>();
            for (MultipartFile file : files) {
                if (file != null && !file.isEmpty()) {
                    // 파일 저장 경로 설정
                    String uploadDir = System.getProperty("user.dir") + "//src//main//resources//imagefiles";
                    File uploadDirFile = new File(uploadDir);

                    // UUID를 사용한 파일 이름 생성
                    String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
                    String filePath = uploadDir + File.separator + fileName;

                    // 파일 저장
                    File dest = new File(filePath);
                    file.transferTo(dest);

                    // 파일 경로와 이름 설정
                    ItemImage itemImage = new ItemImage();
                    itemImage.setFilePath(filePath);
                    itemImage.setFileName(fileName);
                    itemImage.setItem(item);

                    itemImages.add(itemImage);
                    itemImageRepository.save(itemImage);
                    itemImageDTOs.add(convertToDTO(itemImage));
                }
            }
//        itemImageRepository.saveAll(itemImages);
        }

        return itemImageDTOs;
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
        Optional<ItemDetail> optionalItemDetail = itemDetailRepository.findById(id);
        if (optionalItemDetail.isPresent()) {
            ItemDetail itemDetail = optionalItemDetail.get();
            return convertToDTO(itemDetail);
        }
        return null; // Or throw an exception
    }

    // Update ItemDetail
    public ItemDetailDTO updateItemDetail(Long id, ItemDetailDTO itemDetailDTO) {
        Optional<ItemDetail> optionalItemDetail = itemDetailRepository.findById(id);
        if (optionalItemDetail.isPresent()) {
            ItemDetail itemDetail = optionalItemDetail.get();
            itemDetail.setColor(itemDetailDTO.getColor());
            itemDetail.setSize(itemDetailDTO.getSize());
            itemDetail.setQuantity(itemDetailDTO.getQuantity());

            itemDetail = itemDetailRepository.save(itemDetail);
            return convertToDTO(itemDetail);
        }
        return null; // Or throw an exception
    }

    // Delete ItemDetail
    public void deleteItemDetail(Long id) {
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
        Optional<ItemImage> optionalItemImage = itemImageRepository.findById(id);
        if (optionalItemImage.isPresent()) {
            ItemImage itemImage = optionalItemImage.get();
            return convertToDTO(itemImage);
        }
        return null; // Or throw an exception
    }

    public void deleteItemImage(Long id) {itemImageRepository.deleteById(id);}


}