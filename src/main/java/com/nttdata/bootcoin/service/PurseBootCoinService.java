package com.nttdata.bootcoin.service;

import com.nttdata.bootcoin.model.entity.ExchangeRate;
import com.nttdata.bootcoin.model.request.PaymentRequest;
import com.nttdata.bootcoin.model.request.PurseBootcoinRequest;
import com.nttdata.bootcoin.model.response.PaymentResponse;
import com.nttdata.bootcoin.model.response.PurseBootcoinResponse;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Maybe;

public interface PurseBootCoinService {
    Maybe<PurseBootcoinResponse> insert (PurseBootcoinRequest purseBootcoinRequest);
    Flowable<PurseBootcoinResponse> findAll ();
    Maybe<PurseBootcoinResponse> findById (String purseBootCoinId);
    Maybe<PurseBootcoinResponse> updatePurseBootCoinId (String purseBootCoinId,PurseBootcoinRequest purseBootcoinRequest);
    Completable deletePurseBootCoinId(String purseBootCoinId);
    Maybe<ExchangeRate>getExchangeRate();
    Maybe<PaymentResponse>buyBootcoin(PaymentRequest paymentRequest);
    Maybe<PaymentResponse>evaluateDecision(String decision,String idPayment);
}
