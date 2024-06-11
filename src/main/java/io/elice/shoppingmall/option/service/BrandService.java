package io.elice.shoppingmall.option.service;

import io.elice.shoppingmall.option.repository.BrandRepository;
import io.elice.shoppingmall.option.dto.BrandDto;
import io.elice.shoppingmall.option.entity.Brand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BrandService {
    private BrandRepository brandRepository;
    @Autowired
    public BrandService(BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }

    //  브랜드 모두 조회
    public List<BrandDto> getAllBrands() {
        List<Brand> brands = brandRepository.findAll();
        List<BrandDto> brandDtos = new ArrayList<>();

        for (Brand brand : brands) {
            brandDtos.add(brand.entityToDto());
        }
        return brandDtos;
    }

    //  한개의 브랜드 조회
    public BrandDto getBrand(Long id) {
        Brand brand = brandRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("존재하지 않는 브랜드입니다: " + id));
        return brand.entityToDto();
    }

    //  브랜드 생성
    public BrandDto createBrand(BrandDto brandDto) {
        if (brandRepository.existsByName(brandDto.getName())) {
            throw new IllegalArgumentException("이미 존재하는 브랜드입니다: " + brandDto.getName());
        }
        Brand brand = brandDto.dtoToEntity();
        brandRepository.save(brand);
        return brand.entityToDto();
    }

    //  브랜드 업데이트(수정)
    public BrandDto updateBrand(Long id, BrandDto brandDto) {
        Brand brand = brandRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("존재하지 않는 브랜드입니다: " + id));
        brand.setName(brandDto.getName());
        return brand.entityToDto();
    }

    //  브렌드 삭제
    public void deleteBrand(Long id) {
        Brand brand = brandRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("존재하지 않는 브랜드입니다: " + id));
        brandRepository.delete(brand);
    }
}
