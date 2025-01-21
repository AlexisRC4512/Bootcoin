package com.nttdata.bootcoin.model.response;

import com.nttdata.bootcoin.model.enums.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentResponse {
    private String id;
    private double amount;
    private PaymentMethod paymentMethod;
    private String state;
    private Date date;
    private String numberPhone;
    private String numberAccount;
    private String idPurseBuy;
    private String idPurseSeller;
}
