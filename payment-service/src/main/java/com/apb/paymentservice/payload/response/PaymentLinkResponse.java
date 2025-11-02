package com.apb.paymentservice.payload.response;

import lombok.Data;

@Data
public class PaymentLinkResponse {
    private String paymentLinkURL;
    private String PaymentLinkId;
}
