package io.elice.shoppingmall.category.service;

import io.elice.shoppingmall.category.dto.CategoryDto;
import io.elice.shoppingmall.category.entity.Category;
import io.elice.shoppingmall.category.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class CategoryService {
    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }


    //  모든 카테고리 조회
    public List<CategoryDto> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        List<CategoryDto> categoryDtos = new ArrayList<>();

        for (Category category : categories) {
            categoryDtos.add(entityToDto(category));
        }
        return categoryDtos;
    }

    // 특정상위 카테고리의 하위 카테고리 조회
    public List<CategoryDto> getSubCategories(Long parentId) {
        Category parentCategory = categoryRepository.findById(parentId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상위 카테고리입니다: " + parentId));

        List<CategoryDto> subcategories = new ArrayList<>();
        for (Category subCategory : parentCategory.getSubCategories()) {
            subcategories.add(entityToDto(subCategory));
        }
        return subcategories;
    }


    //  하나의 카테고리 조회
    public CategoryDto getCategory(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("존재하지 않는 카테고리입니다: " + id));
        return entityToDto(category);
    }

    //  카테고리 추가
    @Transactional(readOnly = false)
    public CategoryDto createCategory(CategoryDto categoryDto) {

        if (categoryRepository.existsByName(categoryDto.getName())) {
            throw new IllegalArgumentException("이미 존재하는 카테고리입니다: " + categoryDto.getName());
        }

        Category categoryEntity = categoryDto.toEntity();
        Category savedCategoryEntity = categoryRepository.save(categoryEntity);
        return entityToDto(savedCategoryEntity);
    }



    //  카테고리 업데이트
    // CategoryDto로 받을지 Stirng으로 받을지 고민
       @Transactional(readOnly = false)
       public CategoryDto updateCategory(Long id, CategoryDto categoryDto) {
    Category category = categoryRepository.findById(id).orElseThrow(() ->
                     new IllegalArgumentException("존재하지 않는 카테고리입니다: " + id));

           category.setName(categoryDto.getName());

              if (categoryDto.getParentId() != null) {
                Category parentCategory = categoryRepository.findById(categoryDto
                        .getParentId()).orElseThrow(() ->
                        new IllegalArgumentException("존재하지 않는 상위 카테고리입니다: " + categoryDto.getParentId()));
                category.setParent(parentCategory);
            } else {
                category.setParent(null);
              }
           Category savedCategory = categoryRepository.save(category);
              return entityToDto(savedCategory);
       }


    //  카테고리 삭제
    //  자식 카테고리가 존재하면 재귀적으로 삭제
    @Transactional
    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("존재하지 않는 카테고리입니다: " + id));

        deleteSubCategories(category);
        categoryRepository.deleteById(id);
    }

    // 자식 카테고리 삭제 메서드
    private void deleteSubCategories(Category category) {
        if (category.getSubCategories() != null && !category.getSubCategories().isEmpty()) {
            category.getSubCategories().forEach(subCategory -> deleteCategory(subCategory.getId()));
        }
    }


    // Entity를 DTO로 변환하는 메서드
    public CategoryDto entityToDto(Category category) {
        return new CategoryDto(category.getName(),
                Optional.ofNullable(category.getParent())
                        .map(Category::getId)
                        .orElse(null));
    }
}
