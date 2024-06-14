package io.elice.shoppingmall.product.DTO.Item;

import io.elice.shoppingmall.product.Entity.Item.Item;
import io.elice.shoppingmall.product.Entity.Item.ItemImages;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
public class ItemDTO {
    private Long id;
    private Long memberId;
    private Long categoryId;
    private Long genderId;
    private Long itemImagesId;
    private Long brandId;
    private String itemName;
    private Integer price;
    private LocalDateTime createdAt;

    private List<MultipartFile> itemImages; // controller 파일 담는용도
    private List<String> originalFileName; // 원본 파일 이름
    private List<String> storedFilename; // 서버 저장용 파일 이름

    public static ItemDTO toItemDTO(Item item, Long genderId, Long itemImagesId, Long brandId) {
        ItemDTO itemDTO = new ItemDTO();
        itemDTO.setId(item.getId());
        itemDTO.setItemName(item.getItemName());
        itemDTO.setPrice(item.getPrice());
        itemDTO.setCreatedAt(item.getCreatedAt());
        itemDTO.setMemberId(item.getMemberId());
        itemDTO.setCategoryId(item.getCategoryId());
        itemDTO.setGenderId(genderId);
        itemDTO.setItemImagesId(itemImagesId);
        itemDTO.setBrandId(brandId);

        List<String> originalFileNameList = new ArrayList<>();
        List<String> storedFileNameList = new ArrayList<>();
        for(ItemImages itemImages : item.getItemImagesList()){
            originalFileNameList.add(itemImages.getOriginalFileName());
            storedFileNameList.add(itemImages.getStoredFileName());
        }
        itemDTO.setOriginalFileName(originalFileNameList);
        itemDTO.setStoredFilename(storedFileNameList);
        return itemDTO;
    }
}
