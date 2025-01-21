package com.nttdata.bootcoin.model.request;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.nttdata.bootcoin.model.enums.PaymentMethod;
import com.nttdata.bootcoin.util.PaymentTypeDeserializer;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Getter
@NoArgsConstructor
public class PaymentRequest {
    @NotNull(message = "State is required")
    @Size(min = 1, message = "State cannot be empty")
    private String state;

    @NotNull(message = "Date is required")
    private Date date;

    @Min(value = 0, message = "The amount must be greater than or equal to 0")
    private double amount;

    @NotNull(message = "Payment method is required")
    @JsonDeserialize(using = PaymentTypeDeserializer.class)
    private PaymentMethod paymentMethod;
    private String numberPhone;
    private String numberAccount;
    private String idPurseBuy;
    private String idPurseSeller;
    public PaymentRequest(String state, Date date, double amount, PaymentMethod paymentMethod, String numberPhone, String numberAccount
            ,String idPurseBuy,String idPurseSeller) {
        this.state = state;
        this.date = date;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.numberPhone = numberPhone;
        this.numberAccount = numberAccount;
        this.idPurseBuy = idPurseBuy;
        this.idPurseSeller = idPurseSeller;
    }


}
