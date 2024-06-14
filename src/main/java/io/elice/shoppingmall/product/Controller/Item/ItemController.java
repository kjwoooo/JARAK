package io.elice.shoppingmall.product.Controller.Item;


import io.elice.shoppingmall.product.DTO.Item.ItemDTO;
import io.elice.shoppingmall.product.DTO.Item.ItemDetailDTO;
import io.elice.shoppingmall.product.Service.Item.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/items")
public class ItemController {

    @Autowired
    private ItemService itemService;

    // Create Item
    @PostMapping
    public ItemDTO createItem(@RequestPart ItemDTO itemDTO, @RequestPart List<MultipartFile> files) throws IOException {
        return itemService.createItem(itemDTO, files);
    }

    // Get All Items
    @GetMapping
    public List<ItemDTO> getAllItems() {
        return itemService.getAllItems();
    }

    // Get Item by ID
    @GetMapping("/{id}")
    public ItemDTO getItemById(@PathVariable Long id) {
        return itemService.getItemById(id);
    }

    // Update Item
    @PutMapping("/{id}")
    public ItemDTO updateItem(@PathVariable Long id, @RequestPart ItemDTO itemDTO, @RequestPart List<MultipartFile> files) throws IOException {
        return itemService.updateItem(id, itemDTO, files);
    }

    // Delete Item
    @DeleteMapping("/{id}")
    public void deleteItem(@PathVariable Long id) {
        itemService.deleteItem(id);
    }

    // Create ItemDetail
    @PostMapping("/{itemId}/details")
    public ItemDetailDTO createItemDetail(@PathVariable Long itemId, @RequestBody ItemDetailDTO itemDetailDTO) {
        itemDetailDTO.setItemId(itemId);
        return itemService.createItemDetail(itemDetailDTO);
    }

    // Get All ItemDetails
    @GetMapping("/details")
    public List<ItemDetailDTO> getAllItemDetails() {
        return itemService.getAllItemDetails();
    }

    // Get ItemDetail by ID
    @GetMapping("/details/{id}")
    public ItemDetailDTO getItemDetailById(@PathVariable Long id) {
        return itemService.getItemDetailById(id);
    }

    // Update ItemDetail
    @PutMapping("/details/{id}")
    public ItemDetailDTO updateItemDetail(@PathVariable Long id, @RequestBody ItemDetailDTO itemDetailDTO) {
        return itemService.updateItemDetail(id, itemDetailDTO);
    }

    // Delete ItemDetail
    @DeleteMapping("/details/{id}")
    public void deleteItemDetail(@PathVariable Long id) {
        itemService.deleteItemDetail(id);
    }
}