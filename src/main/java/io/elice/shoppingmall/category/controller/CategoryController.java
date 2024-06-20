package io.elice.shoppingmall.category.controller;

import io.elice.shoppingmall.category.dto.CategoryDto;
import io.elice.shoppingmall.category.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
@Tag(name = "카테고리 관리", description = "카테고리 관련 API")
public class CategoryController {

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Operation(summary = "모든 카테고리 조회", description = "모든 카테고리를 조회합니다.")
    @GetMapping
    public ResponseEntity<List<CategoryDto>> getAllCategories() {
        return new ResponseEntity<>(categoryService.getAllCategories(), HttpStatus.OK);
    }

    @Operation(summary = "하나의 카테고리 조회", description = "상위 카테고리에서 해당 서브카테고리 조회 가능한지 확인합니다.")
    @GetMapping("/{id}")
    public ResponseEntity<CategoryDto> getCategory(@PathVariable Long id) {
        return new ResponseEntity<>(categoryService.getCategory(id), HttpStatus.OK);
    }

    @Operation(summary = "특정 상위 카테고리의 하위 카테고리 조회", description = "특정 상위 카테고리의 하위 카테고리를 조회합니다.")
    @GetMapping("/{parentId}/subcategories")
    public ResponseEntity<List<CategoryDto>> getSubCategories(@PathVariable Long parentId) {
        return new ResponseEntity<>(categoryService.getSubCategories(parentId), HttpStatus.OK);
    }

    @Operation(summary = "카테고리 추가", description = "새로운 카테고리를 추가합니다.")
    @PostMapping
    public ResponseEntity<CategoryDto> createCategory(@RequestBody CategoryDto categoryDto) {
        return new ResponseEntity<>(categoryService.createCategory(categoryDto), HttpStatus.CREATED);
    }

    @Operation(summary = "카테고리 업데이트", description = "기존의 카테고리를 업데이트합니다.")
    @PutMapping("/{id}")
    public ResponseEntity<CategoryDto> updateCategory(@PathVariable Long id, @RequestBody CategoryDto categoryDto) {
        return new ResponseEntity<>(categoryService.updateCategory(id, categoryDto), HttpStatus.OK);
    }

    @Operation(summary = "카테고리 삭제", description = "카테고리를 삭제합니다.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
