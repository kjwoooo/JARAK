package io.elice.shoppingmall.product.Service.Item;

import io.elice.shoppingmall.product.DTO.Item.ItemDTO;
import io.elice.shoppingmall.product.DTO.Item.ItemDetailDTO;
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

    // Create Item with multiple images
    public ItemDTO createItem(ItemDTO itemDTO, List<MultipartFile> files) throws IOException {
        Item item = new Item();
        item.setItemName(itemDTO.getItemName());
        item.setPrice(itemDTO.getPrice());
        item.setGender(itemDTO.getGender());

        item = itemRepository.save(item);
        itemDTO.setId(item.getId());

        if (files != null && !files.isEmpty()) {
            List<ItemImage> itemImages = new ArrayList<>();
            for (MultipartFile file : files) {
                if (file != null && !file.isEmpty()) {
                    // 파일 저장 경로 설정
                    String uploadDir = "src/main/resources/imagefiles";
                    File uploadDirFile = new File(uploadDir);
                    if (!uploadDirFile.exists()) {
                        uploadDirFile.mkdirs();
                    }

                    // UUID를 사용한 파일 이름 생성
                    String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
                    String filePath = uploadDir + fileName;

                    // 파일 저장
                    File dest = new File(filePath);
                    file.transferTo(dest);

                    // 파일 경로와 이름 설정
                    ItemImage itemImage = new ItemImage();
                    itemImage.setFilePath(filePath);
                    itemImage.setFileName(fileName);
                    itemImage.setItem(item);

                    itemImages.add(itemImage);
                }
            }
            itemImageRepository.saveAll(itemImages);
        }

        return itemDTO;
    }

    // Get All Items
    public List<ItemDTO> getAllItems() {
        List<Item> items = itemRepository.findAll();
        List<ItemDTO> itemDTOs = new ArrayList<>();

        for (Item item : items) {
            ItemDTO itemDTO = new ItemDTO(
                    item.getId(),
                    item.getItemName(),
                    item.getPrice(),
                    item.getGender()
            );
            itemDTOs.add(itemDTO);
        }

        return itemDTOs;
    }

    // Get Item by ID
    public ItemDTO getItemById(Long id) {
        Optional<Item> optionalItem = itemRepository.findById(id);
        if (optionalItem.isPresent()) {
            Item item = optionalItem.get();
            return new ItemDTO(
                    item.getId(),
                    item.getItemName(),
                    item.getPrice(),
                    item.getGender()
            );
        }
        return null; // Or throw an exception
    }


    // Update Item with multiple images
    public ItemDTO updateItem(Long id, ItemDTO itemDTO, List<MultipartFile> files) throws IOException {
        Optional<Item> optionalItem = itemRepository.findById(id);
        if (optionalItem.isPresent()) {
            Item item = optionalItem.get();
            item.setItemName(itemDTO.getItemName());
            item.setPrice(itemDTO.getPrice());
            item.setGender(itemDTO.getGender());

            item = itemRepository.save(item);

            if (files != null && !files.isEmpty()) {
                List<ItemImage> itemImages = new ArrayList<>();
                for (MultipartFile file : files) {
                    if (file != null && !file.isEmpty()) {
                        // 파일 저장 경로 설정
                        String uploadDir = "uploads/";
                        File uploadDirFile = new File(uploadDir);
                        if (!uploadDirFile.exists()) {
                            uploadDirFile.mkdirs();
                        }

                        // UUID를 사용한 파일 이름 생성
                        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
                        String filePath = uploadDir + fileName;

                        // 파일 저장
                        File dest = new File(filePath);
                        file.transferTo(dest);

                        // 파일 경로와 이름 설정
                        ItemImage itemImage = new ItemImage();
                        itemImage.setFilePath(filePath);
                        itemImage.setFileName(fileName);
                        itemImage.setItem(item);

                        itemImages.add(itemImage);
                    }
                }
                itemImageRepository.saveAll(itemImages);
            }

            return new ItemDTO(
                    item.getId(),
                    item.getItemName(),
                    item.getPrice(),
                    item.getGender()
            );
        }
        return null; // Or throw an exception
    }

    // Delete Item
    public void deleteItem(Long id) {
        Optional<Item> optionalItem = itemRepository.findById(id);
        if (optionalItem.isPresent()) {
            Item item = optionalItem.get();

            // 파일 삭제
            List<ItemImage> itemImages = itemImageRepository.findByItemId(id);
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
        Optional<Item> optionalItem = itemRepository.findById(itemDetailDTO.getItemId());
        if (optionalItem.isPresent()) {
            Item item = optionalItem.get();
            ItemDetail itemDetail = new ItemDetail();
            itemDetail.setColor(itemDetailDTO.getColor());
            itemDetail.setSize(itemDetailDTO.getSize());
            itemDetail.setQuantity(itemDetailDTO.getQuantity());
            itemDetail.setItem(item);

            itemDetail = itemDetailRepository.save(itemDetail);
            itemDetailDTO.setId(itemDetail.getId());
            return itemDetailDTO;
        }
        return null; // Or throw an exception
    }

    // Get All ItemDetails
    public List<ItemDetailDTO> getAllItemDetails() {
        List<ItemDetail> itemDetails = itemDetailRepository.findAll();
        List<ItemDetailDTO> itemDetailDTOs = new ArrayList<>();

        for (ItemDetail itemDetail : itemDetails) {
            ItemDetailDTO itemDetailDTO = new ItemDetailDTO(
                    itemDetail.getId(),
                    itemDetail.getColor(),
                    itemDetail.getSize(),
                    itemDetail.getQuantity(),
                    itemDetail.getItem().getId()
            );
            itemDetailDTOs.add(itemDetailDTO);
        }

        return itemDetailDTOs;
    }

    // Get ItemDetail by ID
    public ItemDetailDTO getItemDetailById(Long id) {
        Optional<ItemDetail> optionalItemDetail = itemDetailRepository.findById(id);
        if (optionalItemDetail.isPresent()) {
            ItemDetail itemDetail = optionalItemDetail.get();
            return new ItemDetailDTO(
                    itemDetail.getId(),
                    itemDetail.getColor(),
                    itemDetail.getSize(),
                    itemDetail.getQuantity(),
                    itemDetail.getItem().getId()
            );
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
            return new ItemDetailDTO(
                    itemDetail.getId(),
                    itemDetail.getColor(),
                    itemDetail.getSize(),
                    itemDetail.getQuantity(),
                    itemDetail.getItem().getId()
            );
        }
        return null; // Or throw an exception
    }

    // Delete ItemDetail
    public void deleteItemDetail(Long id) {
        itemDetailRepository.deleteById(id);
    }
}