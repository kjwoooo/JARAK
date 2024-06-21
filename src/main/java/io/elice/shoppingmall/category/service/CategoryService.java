package io.elice.shoppingmall.category.service;

import io.elice.shoppingmall.category.dto.CategoryDto;
import io.elice.shoppingmall.category.entity.Category;
import io.elice.shoppingmall.category.repository.CategoryRepository;
import io.elice.shoppingmall.exception.CustomException;
import io.elice.shoppingmall.exception.ErrorCode;
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
            categoryDtos.add(category.entityToDto());
        }
        return categoryDtos;
    }

    // 특정 상위 카테고리의 하위 카테고리 조회
    public List<CategoryDto> getSubCategories(Long parentId) {
        Category parentCategory = categoryRepository.findById(parentId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_PARENT_CATEGORY));

        List<CategoryDto> subcategories = new ArrayList<>();
        for (Category subCategory : parentCategory.getSubCategories()) {
            subcategories.add(subCategory.entityToDto());
        }
        return subcategories;
    }


    //  하나의 카테고리 조회
    public CategoryDto getCategory(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(() ->
                new CustomException(ErrorCode.NOT_FOUND_CATEGORY));
        return category.entityToDto();
    }

    // 카테고리 추가
    @Transactional(readOnly = false)
    public CategoryDto createCategory(CategoryDto categoryDto) {
        Category categoryEntity = categoryDto.toEntity();

        // 하위 카테고리 일 경우 상위 카테고리의 아이디가 존재하는지 확인
        if (categoryDto.getParentId() != null) {
            Category parentCategory = categoryRepository.findById(categoryDto.getParentId())
                    .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_PARENT_CATEGORY));
            categoryEntity.setParent(parentCategory);

            // 동일한 상위 카테고리 내에서(이름, 부모id가 같은) 중복된 이름의 하위 카테고리가 있는지 확인
            if (categoryRepository.existsByNameAndParentId(categoryDto.getName(), categoryDto.getParentId())) {
                throw new CustomException(ErrorCode.EXIST_CATEGORY_NAME);
            }
        } else {
            // 상위 카테고리 일 경우 중복 체크
            if (categoryRepository.existsByName(categoryDto.getName())) {
                throw new CustomException(ErrorCode.EXIST_CATEGORY_NAME);
            }
        }

        Category savedCategoryEntity = categoryRepository.save(categoryEntity);
        return savedCategoryEntity.entityToDto();
    }


    //  카테고리 업데이트
    @Transactional(readOnly = false)
    public CategoryDto updateCategory(Long id, CategoryDto categoryDto) {
        Category category = categoryRepository.findById(id).orElseThrow(() ->
                new CustomException(ErrorCode.NOT_FOUND_CATEGORY));


        category.setName(categoryDto.getName());

        if (categoryDto.getParentId() != null) {
            Category parentCategory = categoryRepository.findById(categoryDto
                    .getParentId()).orElseThrow(() ->
                    new CustomException(ErrorCode.NOT_FOUND_PARENT_CATEGORY));
            category.setParent(parentCategory);
        } else {
            category.setParent(null);
        }
        Category savedCategory = categoryRepository.save(category);
        return savedCategory.entityToDto();
    }


    //  카테고리 삭제
    //  자식 카테고리가 존재하면 재귀적으로 삭제
    @Transactional
    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(() ->
                new CustomException(ErrorCode.NOT_FOUND_CATEGORY));

        deleteSubCategories(category);
        categoryRepository.deleteById(id);
    }

    // 자식 카테고리 삭제
    private void deleteSubCategories(Category category) {
        if (category.getSubCategories() != null && !category.getSubCategories().isEmpty()) {
            category.getSubCategories().forEach(subCategory -> deleteCategory(subCategory.getId()));
        }
    }

}
