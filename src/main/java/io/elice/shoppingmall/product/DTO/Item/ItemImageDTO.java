package io.elice.shoppingmall.product.DTO.Item;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ItemImageDTO {
    private Long id;
    private String filePath;
    private String fileName;
    private Long itemId;
}