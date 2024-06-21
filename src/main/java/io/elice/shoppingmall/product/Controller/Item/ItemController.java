package io.elice.shoppingmall.product.Controller.Item;


import com.fasterxml.jackson.databind.ObjectMapper;
import io.elice.shoppingmall.exception.ErrorResponse;
import io.elice.shoppingmall.product.DTO.Item.ItemDTO;
import io.elice.shoppingmall.product.DTO.Item.ItemDetailDTO;
import io.elice.shoppingmall.product.DTO.Item.ItemImageDTO;
import io.elice.shoppingmall.product.Repository.Item.ItemImageRepository;
import io.elice.shoppingmall.product.Service.Item.ItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/items")
@Tag(name = "아이템 관리", description = "아이템 관련 API")
public class ItemController {

    @Autowired
    private ItemService itemService;
    private ItemImageRepository itemImageRepository;


    @Operation(summary = "아이템 생성", description = "새로운 아이템을 생성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "생성 성공", content = @Content(schema = @Schema(implementation = ItemDTO.class))),
            @ApiResponse(responseCode = "400", description = "아이템 생성 실패", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping
    public ItemDTO createItem(@RequestPart(value = "itemDTO") String itemDTOStr,
                              @RequestPart(value = "mainFile") MultipartFile mainFile,
                              @RequestPart(value = "subFile") MultipartFile subFile) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        ItemDTO itemDTO = objectMapper.readValue(itemDTOStr, ItemDTO.class);
        return itemService.createItem(itemDTO, mainFile, subFile);
    }


    // Get All Items
    @Operation(summary = "모든 아이템 조회", description = "모든 아이템을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = ItemDTO.class)))
    })
    @GetMapping
    public List<ItemDTO> getAllItems() {
        return itemService.getAllItems();
    }


    // Get Item by ID
    @Operation(summary = "아이템 ID로 조회", description = "ID로 특정 아이템을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = ItemDTO.class))),
            @ApiResponse(responseCode = "404", description = "아이템을 찾을 수 없음", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{id}")
    public ItemDTO getItemById(@PathVariable("id") Long id) {
        return itemService.getItemById(id);
    }


    @Operation(summary = "아이템 수정", description = "기존의 아이템을 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "수정 성공"),
            @ApiResponse(responseCode = "404", description = "아이템을 찾을 수 없음", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })

    @PutMapping("/{id}")
    public ItemDTO updateItem(@PathVariable Long id,
                              @RequestPart(value = "itemDTO") ItemDTO itemDTO,
                              @RequestPart(value = "mainFile") MultipartFile mainFile,
                              @RequestPart(value = "subFile") MultipartFile subFile) throws IOException {
        return itemService.updateItem(id, itemDTO, mainFile, subFile);
    }

    // Delete Item
    @Operation(summary = "아이템 삭제", description = "기존의 아이템을 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "삭제 성공"),
            @ApiResponse(responseCode = "404", description = "아이템을 찾을 수 없음", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/{id}")
    public void deleteItem(@PathVariable Long id) {
        itemService.deleteItem(id);
    }

    // Create ItemDetail
    @Operation(summary = "아이템 상세 정보 생성", description = "특정 아이템의 상세 정보를 생성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "생성 성공", content = @Content(schema = @Schema(implementation = ItemDetailDTO.class))),
            @ApiResponse(responseCode = "400", description = "아이템 상세 정보 생성 실패", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/{itemId}/details")
    public ItemDetailDTO createItemDetail(@PathVariable Long itemId, @RequestBody ItemDetailDTO itemDetailDTO) {
        itemDetailDTO.setItemId(itemId);
        return itemService.createItemDetail(itemDetailDTO);
    }

    // Get All ItemDetails
    @Operation(summary = "아이템의 모든 상세 정보 조회", description = "특정 아이템의 모든 상세 정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = ItemDetailDTO.class)))
    })
    @GetMapping("/{itemId}/details")
    public List<ItemDetailDTO> getAllItemDetails(@PathVariable Long itemId) {
        return itemService.getAllItemDetails(itemId);
    }

    // Get ItemDetail by ID
    @Operation(summary = "아이템 상세 정보 ID로 조회", description = "ID로 특정 아이템의 상세 정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = ItemDetailDTO.class))),
            @ApiResponse(responseCode = "404", description = "아이템 상세 정보를 찾을 수 없음", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{itemId}/details/{id}")
    public ItemDetailDTO getItemDetailById(@PathVariable Long id) {
        return itemService.getItemDetailById(id);
    }

    // Update ItemDetail
    @Operation(summary = "아이템 상세 정보 수정", description = "기존의 아이템 상세 정보를 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "수정 성공"),
            @ApiResponse(responseCode = "404", description = "아이템 상세 정보를 찾을 수 없음", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PutMapping("/{itemId}/details/{id}")
    public ItemDetailDTO updateItemDetail(@PathVariable Long id, @RequestBody ItemDetailDTO itemDetailDTO) {
        return itemService.updateItemDetail(id, itemDetailDTO);
    }

    // Delete ItemDetail
    @Operation(summary = "아이템 상세 정보 삭제", description = "기존의 아이템 상세 정보를 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "삭제 성공"),
            @ApiResponse(responseCode = "404", description = "아이템 상세 정보를 찾을 수 없음", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/{itemId}/details/{id}")
    public void deleteItemDetail(@PathVariable Long id) {
        itemService.deleteItemDetail(id);
    }


    @Operation(summary = "아이템의 모든 이미지 조회", description = "특정 아이템의 모든 이미지를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = ItemImageDTO.class)))
    })
    @GetMapping("/{itemId}/itemimages")
    public List<ItemImageDTO> getAllItemImages(@PathVariable Long itemId){
        return itemService.getAllItemImages(itemId);
    }

    @Operation(summary = "아이템 이미지 ID로 조회", description = "ID로 특정 아이템의 이미지를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = ItemImageDTO.class))),
            @ApiResponse(responseCode = "404", description = "아이템 이미지를 찾을 수 없음", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{itemId}/itemimages/{id}")
    public ItemImageDTO getItemImage(@PathVariable Long id){
        return itemService.getItemImageById(id);
    }

    @Operation(summary = "아이템 이미지 삭제", description = "기존의 아이템 이미지를 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "삭제 성공"),
            @ApiResponse(responseCode = "404", description = "아이템 이미지를 찾을 수 없음", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/{itemId}/itemimages/{id}")
    public void deleteItemImage(@PathVariable Long id) {itemService.deleteItemImage(id);}
}
