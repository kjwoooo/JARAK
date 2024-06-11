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

    private String name;

    public Brand dtoToEntity() {
        Brand brand = new Brand();
        brand.setName(this.name);
        return brand;
    }
}
