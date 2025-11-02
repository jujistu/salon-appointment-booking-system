package com.apb.categoryservice.controller;

import com.apb.categoryservice.dto.SalonDTO;
import com.apb.categoryservice.model.Category;
import com.apb.categoryservice.service.CategoryService;
import com.apb.categoryservice.service.client.SalonFeignClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;
    private final SalonFeignClient salonFeignClient;

    @GetMapping("/salon/{salonId}")
    public ResponseEntity<Set<Category>> getCategoriesBySalon(@PathVariable Long salonId) throws Exception {
        Set<Category> categories = categoryService.getAllCategoriesBySalonId(salonId);
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<Category> getCategoryById(@PathVariable Long categoryId) throws Exception {
        Category category = categoryService.getCategoryById(categoryId);
        return ResponseEntity.ok(category);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long id, @RequestHeader("Authorization") String jwt) throws Exception {
        SalonDTO salonDTO = salonFeignClient.getSalonsByOwnerId(jwt).getBody();
        categoryService.deleteCategoryById(id, salonDTO.getId());
        return null;
    }
}
