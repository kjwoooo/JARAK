package io.elice.shoppingmall.option.dto;

import io.elice.shoppingmall.option.entity.Brand;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class BrandDto {
    private Long id;
    private String name;

    public Brand dtoToEntity() {
        Brand brand = new Brand();
        brand.setId(this.id); // ID 설정 추가
        brand.setName(this.name);
        return brand;
    }
}
