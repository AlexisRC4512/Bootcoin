package com.nttdata.bootcoin.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExchangeRate {
    private double exchangeRateBuy;
    private double exchangeRateSell;
}
