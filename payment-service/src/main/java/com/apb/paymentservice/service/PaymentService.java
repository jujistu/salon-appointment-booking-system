package com.apb.paymentservice.service;

import com.apb.paymentservice.model.PaymentMethod;
import com.apb.paymentservice.model.PaymentOrder;
import com.apb.paymentservice.payload.dto.BookingDTO;
import com.apb.paymentservice.payload.dto.UserDTO;
import com.apb.paymentservice.payload.response.PaymentLinkResponse;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentLink;

public interface PaymentService {
    PaymentLinkResponse createOrder(UserDTO user, BookingDTO booking, PaymentMethod method) throws StripeException;
    PaymentOrder getPaymentOrderById(Long id) throws Exception;
    PaymentOrder getPaymentOrderByPaymentId(String paymentId);
    PaymentLink createStripePaymentLink(UserDTO user, Double amount, Long orderId) throws StripeException;
    boolean proceedPayment(PaymentOrder order, String paymentId, String paymentUrlId);
}
