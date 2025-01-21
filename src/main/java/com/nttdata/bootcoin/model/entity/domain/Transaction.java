package com.nttdata.bootcoin.model.entity.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {
    private String idTransaction;
    private Double amount;
    private String state;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MMM dd, yyyy, h:mm:ss a", locale = "en")
    private Date date;
}
