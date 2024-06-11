package io.elice.shoppingmall.option.entity;

import io.elice.shoppingmall.option.dto.BrandDto;
import io.elice.shoppingmall.entity.baseEntity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class Brand extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    public BrandDto entityToDto() {
        return new BrandDto(this.name);
    }
}
