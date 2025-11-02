package com.apb.offeringservice.service;

import com.apb.offeringservice.dto.CategoryDTO;
import com.apb.offeringservice.dto.SalonDTO;
import com.apb.offeringservice.dto.ServiceDTO;
import com.apb.offeringservice.model.ServiceOffering;

import java.util.Set;

public interface ServiceOfferingService {
    ServiceOffering createService(SalonDTO salonDTO, ServiceDTO serviceDTO, CategoryDTO categoryDTO);
    ServiceOffering updateService(Long serviceId, ServiceOffering service) throws Exception;
    Set<ServiceOffering> getAllServicesBySalonId(Long salonId, Long categoryId);
    Set<ServiceOffering> getAllServicesById(Set<Long> ids);
    ServiceOffering getServiceById(Long serviceId) throws Exception;
}
