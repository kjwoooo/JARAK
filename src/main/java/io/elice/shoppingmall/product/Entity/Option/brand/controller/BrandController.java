package io.elice.shoppingmall.product.Entity.Option.brand.controller;

import io.elice.shoppingmall.product.Entity.Option.brand.dto.BrandDto;
import io.elice.shoppingmall.product.Entity.Option.brand.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/brands")
public class BrandController {

    private final BrandService brandService;
    @Autowired
    public BrandController(BrandService brandService) {
        this.brandService = brandService;
    }


    /**모든 브랜드 조회*/
    @GetMapping
    public ResponseEntity<List<BrandDto>> getAllBrands() {
        List<BrandDto> brands = brandService.getAllBrands();
        return new ResponseEntity<>(brands, HttpStatus.OK);
    }

    /**하나의 브랜드 조회*/
    @GetMapping("/{id}")
    public ResponseEntity<BrandDto> getbrand(@PathVariable Long id) {
        BrandDto brand = brandService.getBrand(id);
        return new ResponseEntity<>(brand, HttpStatus.OK);
    }

    /**브랜드 생성*/
    @PostMapping
    public ResponseEntity<BrandDto> createBrand(@RequestBody BrandDto brandDto) {
        BrandDto createdBrand = brandService.createBrand(brandDto);
        return new ResponseEntity<>(createdBrand, HttpStatus.CREATED);
    }

    /**브랜드 수정*/
    @PutMapping("/{id}")
    public ResponseEntity<BrandDto> updateBrand(@PathVariable Long id, @RequestBody BrandDto brandDto) {
        BrandDto updatedBrand = brandService.updateBrand(id, brandDto);
        return new ResponseEntity<>(updatedBrand, HttpStatus.OK);
    }

    /**브랜드 삭제*/
    @DeleteMapping("/{id}")
    public ResponseEntity<BrandDto> deleteBrand(@PathVariable Long id) {
        brandService.deleteBrand(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
