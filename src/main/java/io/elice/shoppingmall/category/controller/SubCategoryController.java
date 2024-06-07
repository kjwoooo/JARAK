package io.elice.shoppingmall.category.controller;

import io.elice.shoppingmall.category.dto.SubCategoryDto;
import io.elice.shoppingmall.category.service.SubCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/subCategories")
public class SubCategoryController {
    private final SubCategoryService subCategoryService;

    @Autowired
    public SubCategoryController(SubCategoryService subCategoryService) {
        this.subCategoryService = subCategoryService;
    }

    /** 모든 서브 카테고리 조회<p>
     * *모든 서브 카테고리를 조회할 경우가 있는지 확인*
     *  */
    public ResponseEntity<List<SubCategoryDto>> getAllSubCategories() {
        List<SubCategoryDto> subCategories = subCategoryService.getAllSubCategories();
        return new ResponseEntity<>(subCategories, HttpStatus.OK);
    }

    /**특정 상위 카테고리에 해당하는 <P>
     * 하위(서브) 카테고리 조회
     * */
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<SubCategoryDto>> getSubCategoriesByCategory(@PathVariable Long categoryId) {
        List<SubCategoryDto> subCategoryDto = subCategoryService.getSubCategoriesByCategoryId(categoryId);
        return new ResponseEntity<>(subCategoryDto, HttpStatus.OK);
    }

    /**서브 카테고리 추가*/
    @PostMapping
    public ResponseEntity<SubCategoryDto> createSubCategory(@RequestBody SubCategoryDto subCategoryDto) {
        SubCategoryDto createdSubCategory = subCategoryService.createSubCategory(subCategoryDto);
        return new ResponseEntity<>(createdSubCategory, HttpStatus.CREATED);
    }

    /**서브 카테고리 업데이트*/
    @PutMapping("/{id}")
    public ResponseEntity<SubCategoryDto> updateSubCategory(@PathVariable Long id, SubCategoryDto subCategoryDto) {
        SubCategoryDto updatedSubCategory = subCategoryService.updateSubCategory(id, subCategoryDto);
        return new ResponseEntity<>(updatedSubCategory, HttpStatus.OK);
    }

    /**서브 카테고리 삭제*/
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubCategory(@PathVariable Long id) {
        subCategoryService.deleteSubCategory(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
