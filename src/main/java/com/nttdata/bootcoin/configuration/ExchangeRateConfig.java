package com.nttdata.bootcoin.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ExchangeRateConfig {

    @Value("${exchange.rate.buy}")
    private double exchangeRateBuy;

    @Value("${exchange.rate.sell}")
    private double exchangeRateSell;

    public double getExchangeRateBuy() {
        return exchangeRateBuy;
    }

    public double getExchangeRateSell() {
        return exchangeRateSell;
    }
}
