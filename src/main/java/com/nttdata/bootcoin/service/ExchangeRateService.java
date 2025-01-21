package com.nttdata.bootcoin.service;

import com.nttdata.bootcoin.configuration.ExchangeRateConfig;
import com.nttdata.bootcoin.model.entity.ExchangeRate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExchangeRateService {

    @Autowired
    private ExchangeRateConfig exchangeRateConfig;

    public ExchangeRate getExchangeRate() {
        return ExchangeRate.builder()
                .exchangeRateBuy(exchangeRateConfig.getExchangeRateBuy())
                .exchangeRateSell(exchangeRateConfig.getExchangeRateSell())
                .build();
    }
}