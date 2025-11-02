package com.apb.bookingservice.service.client;

import com.apb.bookingservice.dto.BookingDTO;
import com.apb.bookingservice.dto.PaymentLinkResponse;
import com.apb.bookingservice.model.PaymentMethod;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("PAYMENT-SERVICE")
public interface PaymentFeignClient {
    @PostMapping("/api/payments/create")
    public ResponseEntity<PaymentLinkResponse> createPaymentLink(@RequestBody BookingDTO booking, @RequestParam PaymentMethod method, @RequestHeader("Authorization") String jwt) throws Exception;
}
