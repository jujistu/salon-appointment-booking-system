package com.apb.categoryservice.impl;

import com.apb.categoryservice.dto.SalonDTO;
import com.apb.categoryservice.model.Category;
import com.apb.categoryservice.repository.CategoryRepository;
import com.apb.categoryservice.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public Category saveCategory(Category category, SalonDTO salonDTO) {
        Category newCategory = new Category();
        newCategory.setName(category.getName());
        newCategory.setSalonId(salonDTO.getId());
        newCategory.setImage(category.getImage());
        return categoryRepository.save(newCategory);
    }

    @Override
    public Set<Category> getAllCategoriesBySalonId(Long salonId) {
        return categoryRepository.findBySalonId(salonId);
    }

    @Override
    public Category getCategoryById(Long categoryId) throws Exception {
        Category category = categoryRepository.findById(categoryId).orElse(null);
        if (category == null) {
            throw new Exception("Category with ID " + " does not exist");
        }
        return category;
    }

    @Override
    public void deleteCategoryById(Long categoryId, Long salonId) throws Exception {
        Category category = getCategoryById(categoryId);
        if(category.getSalonId().equals(salonId)) {
            categoryRepository.deleteById(categoryId);
        } else {
            throw new Exception("You do not have permission to delete this category");
        }
    }

    @Override
    public Category findByIdAndSalonId(Long id, Long salonId) throws Exception {
        Category category = categoryRepository.findByIdAndSalonId(id, salonId);
        if (category == null) {
            throw new Exception("Category not found");
        }
        return category;
    }
}
