package io.elice.shoppingmall.product.Entity.Option.brand.dto;

import io.elice.shoppingmall.product.Entity.Option.brand.entity.Brand;
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
