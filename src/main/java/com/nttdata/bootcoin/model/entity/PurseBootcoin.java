package com.nttdata.bootcoin.model.entity;

import com.nttdata.bootcoin.model.entity.domain.Transaction;
import com.nttdata.bootcoin.model.enums.TypeDocument;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "pursebootcoin")
public class PurseBootcoin {
    @Id
    private String id;
    private TypeDocument typeDocument;
    private Integer numberDocument;
    private String numberPhone;
    private String numberAccount;
    private int imeiPhone;
    private String email;
    private double balance;
    private List<Transaction> transactions;

}