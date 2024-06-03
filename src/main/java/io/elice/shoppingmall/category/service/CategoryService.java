package io.elice.shoppingmall.category.service;

import io.elice.shoppingmall.category.dto.CategoryDto;
import io.elice.shoppingmall.category.entity.Category;
import io.elice.shoppingmall.category.repository.CategoryRepository;
import io.elice.shoppingmall.category.repository.SubCategoryRepository;
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
            categoryDtos.add(new CategoryDto(category));
        }
        return categoryDtos;
    }


    //  하나의 카테고리 조회
    public CategoryDto getCategory(Long id) {
        Category category = categoryRepository.findById(id).orElse(null);
        return new CategoryDto(category);
    }


    //  카테고리 추가
    @Transactional(readOnly = false)
    public CategoryDto createCategory(CategoryDto categoryDto) {
        // 해당하는 이름이 이미 존재하는 경우에 대한 예외
        if (categoryRepository.existsByName(categoryDto.getName())) {
            throw new IllegalArgumentException("이미 존재하는 카테고리입니다: " + categoryDto.getName());
        }
        Category category = new Category();
        category.setName(categoryDto.getName());
        categoryRepository.save(category);

        return new CategoryDto(category);
    }


    //  카테고리 업데이트
    @Transactional(readOnly = false)
    public CategoryDto updateCategory(Long id, CategoryDto categoryDto) {
        Category category = categoryRepository.findById(id).orElse(null);
        if (category == null) {
            throw new IllegalArgumentException("존재하지 않는 카테고리입니다: " + id);
        }
        category.setName(categoryDto.getName());
        categoryRepository.save(category);
        return new CategoryDto(category);
    }


    //  카테고리 삭제
    @Transactional(readOnly = false)
    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id).orElse(null);
        if (category == null) {
            throw new IllegalArgumentException("존재하지 않는 카테고리입니다: " + id);
        }
        categoryRepository.delete(category);
    }
}
