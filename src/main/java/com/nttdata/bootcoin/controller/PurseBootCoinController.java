package com.nttdata.bootcoin.controller;

import com.nttdata.bootcoin.model.entity.ExchangeRate;
import com.nttdata.bootcoin.model.request.PaymentRequest;
import com.nttdata.bootcoin.model.request.PurseBootcoinRequest;
import com.nttdata.bootcoin.model.response.PaymentResponse;
import com.nttdata.bootcoin.model.response.PurseBootcoinResponse;
import com.nttdata.bootcoin.service.PurseBootCoinService;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Maybe;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/purse-bootcoin")
public class PurseBootCoinController {
    private final PurseBootCoinService purseBootCoinService;

    @PostMapping
    public Maybe<PurseBootcoinResponse> insert(@RequestBody PurseBootcoinRequest purseBootcoinRequest) {
        return purseBootCoinService.insert(purseBootcoinRequest);
    }

    @GetMapping
    public Flowable<PurseBootcoinResponse> findAll() {
        return purseBootCoinService.findAll();
    }

    @GetMapping("/{id}")
    public Maybe<PurseBootcoinResponse> findById(@PathVariable String id) {
        return purseBootCoinService.findById(id);
    }

    @PutMapping("/{id}")
    public Maybe<PurseBootcoinResponse> updatePurseBootCoinId(@PathVariable String id, @RequestBody PurseBootcoinRequest purseBootcoinRequest) {
        return purseBootCoinService.updatePurseBootCoinId(id, purseBootcoinRequest);
    }

    @DeleteMapping("/{id}")
    public Completable deletePurseBootCoinId(@PathVariable String id) {
        return purseBootCoinService.deletePurseBootCoinId(id);
    }

    @GetMapping("/exchange-rate")
    public Maybe<ExchangeRate> getExchangeRate() {
        return purseBootCoinService.getExchangeRate();
    }

    @PostMapping("/buy")
    public Maybe<PaymentResponse> buyBootcoin(@RequestBody PaymentRequest paymentRequest) {
        return purseBootCoinService.buyBootcoin(paymentRequest);
    }

    @PostMapping("/evaluate-decision")
    public Maybe<PaymentResponse> evaluateDecision(@RequestParam String decision, @RequestParam String idPayment) {
        return purseBootCoinService.evaluateDecision(decision, idPayment);
    }
}