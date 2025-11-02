package com.apb.bookingservice.dto;

import lombok.Data;

@Data
public class PaymentLinkResponse {
    private String paymentLinkURL;
    private String PaymentLinkId;
}
