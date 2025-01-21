package com.nttdata.bootcoin.model.response;

import com.nttdata.bootcoin.model.entity.domain.Transaction;
import com.nttdata.bootcoin.model.enums.TypeDocument;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PurseBootcoinResponse {
    private String id;
    private TypeDocument typeDocument;
    private Integer numberDocument;
    private String numberPhone;
    private int imeiPhone;
    private String email;
    private double balance;
    private List<Transaction> transactions;
}
