package com.apb.paymentservice.controller;

import com.apb.paymentservice.model.PaymentMethod;
import com.apb.paymentservice.model.PaymentOrder;
import com.apb.paymentservice.payload.dto.BookingDTO;
import com.apb.paymentservice.payload.dto.UserDTO;
import com.apb.paymentservice.payload.response.PaymentLinkResponse;
import com.apb.paymentservice.service.PaymentService;
import com.apb.paymentservice.service.client.UserFeignClient;
import com.stripe.exception.StripeException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;
    private final UserFeignClient userFeignClient;

    @PostMapping("/create")
    public ResponseEntity<PaymentLinkResponse> createPaymentLink(@RequestBody BookingDTO booking, @RequestParam PaymentMethod method,
                                                                 @RequestHeader("Authorization") String jwt) throws Exception {
        UserDTO user = userFeignClient.getUserProfile(jwt).getBody();

        PaymentLinkResponse response = paymentService.createOrder(user, booking, method);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{paymentOrderId}")
    public ResponseEntity<PaymentOrder> getPaymentOrderById(@PathVariable Long paymentOrderId) throws Exception{
        PaymentOrder order = paymentService.getPaymentOrderById(paymentOrderId);
        return ResponseEntity.ok(order);
    }

    @PatchMapping("/proceed")
    public ResponseEntity<Boolean> proceedPayment(@RequestParam String paymentId, @RequestParam String paymentUrlId) throws Exception{

        PaymentOrder paymentOrder = paymentService.getPaymentOrderByPaymentId(paymentId);
        boolean response = paymentService.proceedPayment(paymentOrder, paymentId, paymentUrlId);
        return ResponseEntity.ok(response);
    }

}
