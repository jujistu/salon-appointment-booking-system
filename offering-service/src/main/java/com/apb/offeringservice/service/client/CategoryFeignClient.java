package com.apb.offeringservice.service.client;

import com.apb.offeringservice.dto.CategoryDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("CATEGORY-SERVICE")
public interface CategoryFeignClient {
    @GetMapping("/api/categories/salon-owner/salon/{salonId}/category/{id}")
    public ResponseEntity<CategoryDTO> getCategoryByIdAndSalon(@PathVariable Long salonId, @PathVariable Long id) throws Exception;
}
