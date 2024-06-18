package io.elice.shoppingmall.product.DTO.Item;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemImageDTO {
    private Long id;
    private String filePath;
    private String fileName;
    private Long itemId;
}