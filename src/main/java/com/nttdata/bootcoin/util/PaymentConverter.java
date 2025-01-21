package com.nttdata.bootcoin.util;

import com.nttdata.bootcoin.model.entity.domain.Payment;
import com.nttdata.bootcoin.model.request.PaymentRequest;
import com.nttdata.bootcoin.model.response.PaymentResponse;

import java.util.UUID;

public class PaymentConverter {
    public static Payment toEntity(PaymentRequest paymentRequest) {
        if (paymentRequest == null) {
            return null;
        }

        Payment payment = new Payment();
        payment.setId(UUID.randomUUID().toString());
        payment.setAmount(paymentRequest.getAmount());
        payment.setPaymentMethod(paymentRequest.getPaymentMethod());
        payment.setState(paymentRequest.getState());
        payment.setDate(paymentRequest.getDate());
        payment.setNumberPhone(paymentRequest.getNumberPhone());
        payment.setNumberAccount(paymentRequest.getNumberAccount());
        payment.setIdPurseBuy(paymentRequest.getIdPurseBuy());
        payment.setIdPurseSeller(paymentRequest.getIdPurseSeller());
        return payment;
    }

    public static PaymentResponse toResponse(Payment payment) {
        if (payment == null) {
            return null;
        }

        PaymentResponse paymentResponse = new PaymentResponse();
        paymentResponse.setId(payment.getId());
        paymentResponse.setAmount(payment.getAmount());
        paymentResponse.setPaymentMethod(payment.getPaymentMethod());
        paymentResponse.setState(payment.getState());
        paymentResponse.setDate(payment.getDate());
        paymentResponse.setIdPurseBuy(payment.getIdPurseBuy());
        paymentResponse.setIdPurseSeller(payment.getIdPurseSeller());
        paymentResponse.setNumberAccount(payment.getNumberAccount());
        paymentResponse.setNumberPhone(payment.getNumberPhone());
        return paymentResponse;
    }
}
