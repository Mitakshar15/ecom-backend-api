package com.ainkai.model;


import com.ainkai.user.domain.PaymentMethod;
import com.ainkai.user.domain.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDetails {


    private PaymentMethod paymentMethod;
    private PaymentStatus status;
    private String razorpayPaymentLinkId;
    private String  razorpayPaymentLinkReferenceId;
    private String  razorpayPaymentLinkStatus;
    private String razorpayPaymentId;
}
