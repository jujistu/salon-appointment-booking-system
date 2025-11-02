package com.apb.categoryservice.controller;

import com.apb.categoryservice.dto.SalonDTO;
import com.apb.categoryservice.model.Category;
import com.apb.categoryservice.service.CategoryService;
import com.apb.categoryservice.service.client.SalonFeignClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/categories/salon-owner")
@RequiredArgsConstructor
public class SalonCategoryController {
    private final CategoryService categoryService;
    private final SalonFeignClient salonFeignClient;

    @PostMapping
    public ResponseEntity<Category> createCategory(@RequestBody Category category, @RequestHeader("Authorization") String jwt) throws Exception {
        SalonDTO salonDTO = salonFeignClient.getSalonsByOwnerId(jwt).getBody();
        Category newCategory = categoryService.saveCategory(category, salonDTO);
        return ResponseEntity.ok(newCategory);
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long categoryId) throws Exception {
        SalonDTO salonDTO = new SalonDTO();
        salonDTO.setId(1L);
        categoryService.deleteCategoryById(categoryId, salonDTO.getId());
        return ResponseEntity.ok("Category deleted successfully");
    }

    @GetMapping("/salon/{salonId}/category/{id}")
    public ResponseEntity<Category> getCategoryByIdAndSalon(@PathVariable Long salonId, @PathVariable Long id) throws Exception {
        Category category = categoryService.findByIdAndSalonId(id, salonId);
        return ResponseEntity.ok(category);
    }


}
