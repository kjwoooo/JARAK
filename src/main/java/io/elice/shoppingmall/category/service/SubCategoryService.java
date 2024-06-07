package io.elice.shoppingmall.category.service;

import io.elice.shoppingmall.category.dto.SubCategoryDto;
import io.elice.shoppingmall.category.entity.Category;
import io.elice.shoppingmall.category.entity.SubCategory;
import io.elice.shoppingmall.category.repository.CategoryRepository;
import io.elice.shoppingmall.category.repository.SubCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class SubCategoryService {
    private final SubCategoryRepository subCategoryRepository;
    private final CategoryRepository categoryRepository;

    @Autowired
    SubCategoryService(SubCategoryRepository subCategoryRepository, CategoryRepository categoryRepository) {
        this.subCategoryRepository = subCategoryRepository;
        this.categoryRepository = categoryRepository;
    }

    // 모든 서브 카테고리 조회
    // 모든 서브카테고리를 조회할 경우가 있는지 확인
    public List<SubCategoryDto> getAllSubCategories() {
        List<SubCategory> subCategories = subCategoryRepository.findAll();
        List<SubCategoryDto> subCategoryDtos = new ArrayList<>();

        for (SubCategory subCategory : subCategories) {
            subCategoryDtos.add(new SubCategoryDto(subCategory));
        }
        return subCategoryDtos;
    }

    // 특정 메인 카테고리에 속한 서브 카테고리 조회
    public List<SubCategoryDto> getSubCategoriesByCategoryId(Long categoryId) {
        List<SubCategory> subCategories = subCategoryRepository.findByCategoryId(categoryId);
        List<SubCategoryDto> subCategoryDtos = new ArrayList<>();

        for (SubCategory subCategory : subCategories) {
            subCategoryDtos.add(new SubCategoryDto(subCategory));
        }
        return subCategoryDtos;
    }

    // 서브 카테고리 추가
    @Transactional(readOnly = false)
    public SubCategoryDto createSubCategory(SubCategoryDto subCategoryDto) {
        // subCategoryDto에 해당하는 상위 카테고리를 조회 -> 없으면 예외 처리
        Category category = categoryRepository.findById(subCategoryDto.getCategoryId()).orElseThrow(() ->
                new IllegalArgumentException("존재하지 않는 상위 카테고리입니다: " + subCategoryDto.getCategoryId()));

        //SubCategoryDto 객체를 SubCategory 엔티티로 변환
        SubCategory subCategory = subCategoryDto.toEntity();
        // 상위 카테고리 설정
        subCategory.setCategory(category);
        subCategoryRepository.save(subCategory);
        return new SubCategoryDto(subCategory);
    }

    // 서브 카테고리 업데이트
    @Transactional(readOnly = false)
    public SubCategoryDto updateSubCategory(Long id, SubCategoryDto subCategoryDto) {
        SubCategory subCategory = subCategoryRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("존재하지 않는 서브 카테고리입니다: " + id));

        subCategory.setName(subCategoryDto.getName());
        subCategoryRepository.save(subCategory);
        return new SubCategoryDto(subCategory);

    }

    // 서브 카테고리 삭제
    @Transactional(readOnly = false)
    public void deleteSubCategory(Long id) {
        SubCategory subCategory = subCategoryRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("존재하지 않는 서브 카테고리입니다: " + id));
        subCategoryRepository.delete(subCategory);
    }

}
