package com.nttdata.bootcoin.util;

import com.nttdata.bootcoin.model.entity.PurseBootcoin;
import com.nttdata.bootcoin.model.request.PurseBootcoinRequest;
import com.nttdata.bootcoin.model.response.PurseBootcoinResponse;

import java.util.ArrayList;
import java.util.UUID;

public class PurseBootcoinMapper {

    public static PurseBootcoin toEntity(PurseBootcoinRequest request) {
       return PurseBootcoin.builder()
               .id(UUID.randomUUID().toString())
               .typeDocument(request.getTypeDocument())
               .numberDocument(request.getNumberDocument())
               .numberPhone(request.getNumberPhone())
               .numberAccount(request.getNumberAccount())
               .imeiPhone(request.getImeiPhone())
               .email(request.getEmail())
               .balance(0.0)
               .transactions(new ArrayList<>())
               .build();
    }

    public static PurseBootcoinResponse toResponse(PurseBootcoin entity) {
        if (entity == null) {
            return null;
        }
        return PurseBootcoinResponse.builder()
                .id(entity.getId())
                .balance(entity.getBalance())
                .email(entity.getEmail())
                .imeiPhone(entity.getImeiPhone())
                .numberPhone(entity.getNumberPhone())
                .numberDocument(entity.getNumberDocument())
                .typeDocument(entity.getTypeDocument())
                .transactions(entity.getTransactions())
                .build();
    }
}
