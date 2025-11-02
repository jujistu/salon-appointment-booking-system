package com.apb.paymentservice.service.client;


import com.apb.paymentservice.payload.dto.ServiceDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Set;

@FeignClient("OFFERING-SERVICE")
public interface ServiceOfferingFeignClient {
    @GetMapping("/list/{ids}")
    public ResponseEntity<Set<ServiceDTO>> getServiceByIds(@PathVariable Set<Long> ids) throws Exception;
}
