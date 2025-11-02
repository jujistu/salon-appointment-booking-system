package com.apb.offeringservice.controller;

import com.apb.offeringservice.model.ServiceOffering;
import com.apb.offeringservice.service.ServiceOfferingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/service-offering")
@RequiredArgsConstructor
public class ServiceOfferingController {

    public final ServiceOfferingService serviceOfferingService;

    @GetMapping("/salon/{salonId}")
    public ResponseEntity<Set<ServiceOffering>> getServicesBySalonId(@PathVariable Long salonId, @RequestParam(required = false) Long categoryId) {

        Set<ServiceOffering> serviceOfferings = serviceOfferingService.getAllServicesBySalonId(salonId, categoryId);
        return ResponseEntity.ok(serviceOfferings);
    }

    @GetMapping("/{serviceId}")
    public ResponseEntity<ServiceOffering> getServiceById(@PathVariable Long serviceId) throws Exception {
        ServiceOffering service = serviceOfferingService.getServiceById(serviceId);;
        return ResponseEntity.ok(service);
    }

    @GetMapping("/list/{ids}")
    public ResponseEntity<Set<ServiceOffering>> getServiceByIds(@PathVariable Set<Long> ids) throws Exception {
        Set<ServiceOffering> serviceOfferings = serviceOfferingService.getAllServicesById(ids);
        return ResponseEntity.ok(serviceOfferings);
    }
}
