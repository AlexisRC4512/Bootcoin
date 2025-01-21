package com.nttdata.bootcoin.model.entity.events;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionEvents {
    private String idTransaction;
    private Double amount;
    private String numberAccountSeller;
    private String numberPhoneSeller;
    private String idPurseBuy;
    private String idPurseSeller;
    private String state;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MMM dd, yyyy, h:mm:ss a")
    private Date date;
    private String idPay;

}
