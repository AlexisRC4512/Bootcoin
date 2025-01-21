package com.nttdata.bootcoin.model.entity.domain;

import com.nttdata.bootcoin.model.enums.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "payment")
public class Payment {
    @Id
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
