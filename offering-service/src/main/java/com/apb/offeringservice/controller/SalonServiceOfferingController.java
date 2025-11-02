package com.apb.offeringservice.controller;

import com.apb.offeringservice.dto.CategoryDTO;
import com.apb.offeringservice.dto.SalonDTO;
import com.apb.offeringservice.dto.ServiceDTO;
import com.apb.offeringservice.model.ServiceOffering;
import com.apb.offeringservice.service.ServiceOfferingService;
import com.apb.offeringservice.service.client.CategoryFeignClient;
import com.apb.offeringservice.service.client.SalonFeignClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/service-offering/salon-owner")
public class SalonServiceOfferingController {
    private final ServiceOfferingService serviceOfferingService;
    private final SalonFeignClient salonFeignClient;
    private final CategoryFeignClient categoryFeignClient;

    @PostMapping
    public ResponseEntity<ServiceOffering> createService(@RequestBody ServiceDTO serviceDTO, @RequestHeader("Authorization") String jwt) throws Exception {
        SalonDTO salonDTO = salonFeignClient.getSalonsByOwnerId(jwt).getBody();

        CategoryDTO categoryDTO = categoryFeignClient.getCategoryByIdAndSalon(serviceDTO.getCategoryId(), serviceDTO.getSalonId()).getBody();

        ServiceOffering serviceOffering = serviceOfferingService.createService(salonDTO,serviceDTO,categoryDTO);
        return ResponseEntity.ok(serviceOffering);
    }

    @PutMapping("/{serviceId}")
    public ResponseEntity<ServiceOffering> updateService(@PathVariable Long serviceId, @RequestBody ServiceOffering serviceOffering) throws Exception {
        ServiceOffering updatedServiceOffering = serviceOfferingService.updateService(serviceId,serviceOffering);
        return ResponseEntity.ok(updatedServiceOffering);
    }

}
