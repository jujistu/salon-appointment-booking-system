package com.apb.offeringservice.repository;

import com.apb.offeringservice.model.ServiceOffering;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface ServiceOfferingRepository extends JpaRepository<ServiceOffering, Long> {
    Set<ServiceOffering> findBySalonId(Long salonId);
}
