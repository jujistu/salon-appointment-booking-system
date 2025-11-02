package com.apb.offeringservice.impl;

import com.apb.offeringservice.dto.CategoryDTO;
import com.apb.offeringservice.dto.SalonDTO;
import com.apb.offeringservice.dto.ServiceDTO;
import com.apb.offeringservice.model.ServiceOffering;
import com.apb.offeringservice.repository.ServiceOfferingRepository;
import com.apb.offeringservice.service.ServiceOfferingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ServiceOfferingServiceImpl implements ServiceOfferingService {

    private final ServiceOfferingRepository serviceOfferingRepository;

    @Override
    public ServiceOffering createService(SalonDTO salonDTO, ServiceDTO serviceDTO, CategoryDTO categoryDTO) {
        ServiceOffering serviceOffering = new ServiceOffering();
        serviceOffering.setImage(serviceDTO.getImage());
        serviceOffering.setCategoryId(categoryDTO.getId());
        serviceOffering.setName(serviceDTO.getName());
        serviceOffering.setSalonId(salonDTO.getId());
        serviceOffering.setDescription(serviceDTO.getDescription());
        serviceOffering.setPrice(serviceDTO.getPrice());
        serviceOffering.setDuration(serviceDTO.getDuration());

        return serviceOfferingRepository.save(serviceOffering);
    }

    @Override
    public ServiceOffering updateService(Long serviceId, ServiceOffering service) throws Exception {
        ServiceOffering serviceOffering = serviceOfferingRepository.findById(serviceId).orElse(null);
        if (serviceOffering == null) {
            throw new Exception("Service with ID: "+ serviceId + " does not exist");
        }
        serviceOffering.setImage(service.getImage());
        serviceOffering.setName(service.getName());
        serviceOffering.setDescription(service.getDescription());
        serviceOffering.setPrice(service.getPrice());
        serviceOffering.setDuration(service.getDuration());

        return serviceOfferingRepository.save(serviceOffering);
    }

    @Override
    public Set<ServiceOffering> getAllServicesBySalonId(Long salonId, Long categoryId) {
        Set<ServiceOffering> services = serviceOfferingRepository.findBySalonId(salonId);
        if (categoryId != null) {
            services = services.stream().filter(s -> s.getCategoryId() != null && s.getCategoryId()==categoryId).collect(Collectors.toSet());
        }
        return services;

    }

    @Override
    public Set<ServiceOffering> getAllServicesById(Set<Long> ids) {
        List<ServiceOffering> services = serviceOfferingRepository.findAllById(ids);
        return new HashSet<>(services);
    }

    @Override
    public ServiceOffering getServiceById(Long serviceId) throws Exception {
        ServiceOffering serviceOffering = serviceOfferingRepository.findById(serviceId).orElse(null);
        if (serviceOffering == null) {
            throw new Exception("Service with ID: "+ serviceId + " does not exist");
        }
        return serviceOffering;
    }
}
