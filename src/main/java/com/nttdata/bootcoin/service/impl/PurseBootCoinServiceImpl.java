package com.nttdata.bootcoin.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.nttdata.bootcoin.exception.BootcoinNotFoundException;
import com.nttdata.bootcoin.model.entity.ExchangeRate;
import com.nttdata.bootcoin.model.entity.PurseBootcoin;
import com.nttdata.bootcoin.model.entity.domain.Payment;
import com.nttdata.bootcoin.model.entity.domain.Transaction;
import com.nttdata.bootcoin.model.entity.events.CompleteEvent;
import com.nttdata.bootcoin.model.entity.events.TransactionEvents;
import com.nttdata.bootcoin.model.enums.PaymentMethod;
import com.nttdata.bootcoin.model.request.PaymentRequest;
import com.nttdata.bootcoin.model.request.PurseBootcoinRequest;
import com.nttdata.bootcoin.model.response.PaymentResponse;
import com.nttdata.bootcoin.model.response.PurseBootcoinResponse;
import com.nttdata.bootcoin.repository.BootcoinRepository;
import com.nttdata.bootcoin.repository.PaymentRepository;
import com.nttdata.bootcoin.service.CatalogCacheService;
import com.nttdata.bootcoin.service.ExchangeRateService;
import com.nttdata.bootcoin.service.PurseBootCoinService;
import com.nttdata.bootcoin.util.PaymentConverter;
import com.nttdata.bootcoin.util.PurseBootcoinMapper;
import com.nttdata.bootcoin.util.TransactionConverter;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Maybe;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import reactor.adapter.rxjava.RxJava3Adapter;
import reactor.core.publisher.Mono;

import java.util.*;

@Log4j2
@RequiredArgsConstructor
@Service
public class PurseBootCoinServiceImpl implements PurseBootCoinService {
    private final BootcoinRepository bootcoinRepository;
    private final ExchangeRateService exchangeRateService;
    private final PaymentRepository paymentRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final CatalogCacheService catalogCacheService;

    @Override
    public Maybe<PurseBootcoinResponse> insert(PurseBootcoinRequest purseBootcoinRequest) {
        return RxJava3Adapter.monoToMaybe(bootcoinRepository.save(PurseBootcoinMapper.toEntity(purseBootcoinRequest)).map(PurseBootcoinMapper::toResponse)
                .doOnError(e -> log.error("Error Create PurseBootcoin : {}",e))
                .onErrorMap(e -> new Exception("Error Create PurseBootcoin", e)));

    }

    @Override
    public Flowable<PurseBootcoinResponse> findAll() {
        return  RxJava3Adapter.fluxToFlowable(bootcoinRepository.findAll().map(PurseBootcoinMapper::toResponse)
                .doOnError(e -> log.error("Error fetching PurseBootcoin : {}",e))
                .onErrorMap(e -> new Exception("Error fetching PurseBootcoin", e)));
    }

    @Override
    public Maybe<PurseBootcoinResponse> findById(String purseBootCoinId) {
        return RxJava3Adapter.monoToMaybe(catalogCacheService.getCatalog(purseBootCoinId)
                        .doOnNext(value -> log.debug("Valor obtenido de Redis para la clave {}: {}", purseBootCoinId, value))
                .switchIfEmpty(bootcoinRepository.findById(purseBootCoinId)
                        .flatMap(bootcoin -> catalogCacheService.setCatalog(purseBootCoinId, bootcoin)
                                .doOnSuccess(success -> log.debug("Valor guardado en Redis para la clave {}: {}", purseBootCoinId, bootcoin))
                                .thenReturn(bootcoin)))
                .map(purseEntity -> PurseBootcoinMapper.toResponse((PurseBootcoin) purseEntity)));
    }

    @Override
    public Maybe<PurseBootcoinResponse> updatePurseBootCoinId(String purseBootCoinId, PurseBootcoinRequest purseBootcoinRequest) {
        return RxJava3Adapter.monoToMaybe( bootcoinRepository.findById(purseBootCoinId)
                .switchIfEmpty(Mono.error(new BootcoinNotFoundException("No purseBootCoin found with " + purseBootCoinId)))
                .flatMap(existingPurse -> {
                    catalogCacheService.setCatalog(purseBootCoinId,existingPurse);
                    PurseBootcoin updatedPurseBootCoin = PurseBootcoinMapper.toEntity(purseBootcoinRequest);
                    updatedPurseBootCoin.setId(existingPurse.getId());
                    return bootcoinRepository.save(updatedPurseBootCoin);
                })
                .map(PurseBootcoinMapper::toResponse)
        );
    }

    @Override
    public Completable deletePurseBootCoinId(String purseBootCoinId) {
        return RxJava3Adapter.monoToCompletable(bootcoinRepository.deleteById(purseBootCoinId).then(
                                catalogCacheService.deleteCatalog(purseBootCoinId)
                        )
                .switchIfEmpty(Mono.error(new BootcoinNotFoundException("No PurseBootCoinId found with" + purseBootCoinId)))
                .doOnError(e -> log.error("Error delete PurseBootCoinId  with id: {}", purseBootCoinId, e))
                .onErrorMap(e -> new Exception("Error delete PurseBootCoinId by id", e))
        );
    }

    @Override
    public Maybe<ExchangeRate> getExchangeRate() {
        return Maybe.just(exchangeRateService.getExchangeRate())
                .doOnError(e -> {
                    log.error("Error get Exchange Rate", e);
                    new Exception("Error get Exchange Rate");
                });


    }

    @Override
    public Maybe<PaymentResponse> buyBootcoin(PaymentRequest paymentRequest) {
        return RxJava3Adapter.monoToMaybe(paymentRepository.save(PaymentConverter.toEntity(paymentRequest))
                .map(PaymentConverter::toResponse));
    }

    @Override
    public Maybe<PaymentResponse> evaluateDecision(String decision, String idPayment) {
        return RxJava3Adapter.monoToMaybe(paymentRepository.findById(idPayment)
                .switchIfEmpty(Mono.error(new Exception("No existe la solicitud de compra con ese id")))
                .flatMap(payment -> {
                    if (decision.equalsIgnoreCase("ACCEPT")) {
                        return handleAcceptDecision(payment);
                    } else if (decision.equalsIgnoreCase("REJECT")) {
                        payment.setState("REJECT");
                        paymentRepository.save(payment);
                        return Mono.just(PaymentConverter.toResponse(payment));
                    }
                    return Mono.error(new Exception("Invalid decision"));
                }));
    }
    private Mono<PaymentResponse> handleAcceptDecision(Payment payment) {
        payment.setState("ACCEPT");
        return paymentRepository.save(payment)
                .then(Mono.defer(() -> {
                    TransactionEvents transactionEvents = createTransactionEvent(payment);
                    Gson gson = new Gson();
                    String jsonTransactionEvent = gson.toJson(transactionEvents);
                    sendTransactionEvent(payment, jsonTransactionEvent);
                    return updatePurseBootcoinBuy(payment, transactionEvents)
                            .then(Mono.just(PaymentConverter.toResponse(payment)));
                }));
    }
    private TransactionEvents createTransactionEvent(Payment payment) {
        TransactionEvents transactionEvents = new TransactionEvents();
        transactionEvents.setIdPay(payment.getId());
        transactionEvents.setIdTransaction(UUID.randomUUID().toString());
        transactionEvents.setIdPurseBuy(payment.getIdPurseBuy());
        transactionEvents.setIdPurseSeller(payment.getIdPurseSeller());
        transactionEvents.setNumberAccountSeller(payment.getNumberAccount());
        transactionEvents.setNumberPhoneSeller(payment.getNumberPhone());
        transactionEvents.setDate(new Date());
        transactionEvents.setAmount(payment.getAmount() * exchangeRateService.getExchangeRate().getExchangeRateSell());
        transactionEvents.setState("INITIAL");
        return transactionEvents;
    }

    private Mono<Void> updatePurseBootcoinBuy(Payment payment, TransactionEvents transactionEvents) {
        return Mono.defer(() -> {
            Mono<PurseBootcoin> purseBuyMono = bootcoinRepository.findById(transactionEvents.getIdPurseBuy())
                    .switchIfEmpty(Mono.error(new RuntimeException("Purse Buy not found")));
            Mono<PurseBootcoin> purseSellMono = bootcoinRepository.findById(transactionEvents.getIdPurseSeller())
                    .switchIfEmpty(Mono.error(new RuntimeException("Purse Sell not found")));

            return Mono.zip(purseBuyMono, purseSellMono)
                    .flatMap(tuple -> {
                        PurseBootcoin purseBuy = tuple.getT1();
                        PurseBootcoin purseSell = tuple.getT2();

                        purseBuy.setBalance(purseBuy.getBalance() + payment.getAmount());
                        purseBuy.setTransactions(addPayment(purseBuy, TransactionConverter.toTransation(transactionEvents)));

                        purseSell.setBalance(purseSell.getBalance() - payment.getAmount());
                        purseSell.setTransactions(addPayment(purseSell, TransactionConverter.toTransation(transactionEvents)));
                        catalogCacheService.setCatalog(purseBuy.getId(),purseBuy);
                        catalogCacheService.setCatalog(purseSell.getId(),purseSell);

                        return Mono.when(
                                bootcoinRepository.save(purseBuy),
                                bootcoinRepository.save(purseSell)
                        ).then();
                    })
                    .onErrorResume(e -> {
                        log.error("Error updating purseBootcoinBuy: {}", e.getMessage());
                        return Mono.error(e);
                    });
        });
    }
    private void sendTransactionEvent(Payment payment, String jsonTransactionEvent) {
        if (payment.getPaymentMethod().name().equalsIgnoreCase(PaymentMethod.YANKI.name())) {
            kafkaTemplate.send("bootcoin-pay-yanki", jsonTransactionEvent);
        } else if (payment.getPaymentMethod().name().equalsIgnoreCase(PaymentMethod.TRANSFER.name())) {
            kafkaTemplate.send("bootcoin-pay-transfer", jsonTransactionEvent);
        }
    }

    @KafkaListener( id = "myConsumer1", topics = "bootcoin-pay-yanki-complete", groupId = "springboot-group-1", autoStartup = "true")
    public void listenMessageYanki(String message) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            log.info("Message received from Kafka: {}", message);
            CompleteEvent completeEvent = objectMapper.readValue(message, CompleteEvent.class);
            processPaymentStatusEvent(completeEvent);
            processTransationEvent(completeEvent);
        } catch (Exception e) {
            log.error("Error processing message: {}", message, e);
        }
    }
    @KafkaListener(id = "myConsumer",topics = "bootcoin-pay-transfer-complete", groupId = "springboot-group-1", autoStartup = "true")
    public void listenMessageTransfer(String message) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            log.info("Message received from Kafka: {}", message);
            CompleteEvent completeEvent = objectMapper.readValue(message, CompleteEvent.class);
            processPaymentStatusEvent(completeEvent);
            processTransationEvent(completeEvent);
        } catch (Exception e) {
            log.error("Error processing message: {}", message, e);
        }
    }
    private void processPaymentStatusEvent(CompleteEvent completeEvent) {
        paymentRepository.findById(completeEvent.getIdPay()).flatMap(payment -> {
            payment.setState(completeEvent.getState());
            return paymentRepository.save(payment);
        }).subscribe();
    }
    private void processTransationEvent(CompleteEvent completeEvent) {
        bootcoinRepository.findById(completeEvent.getIdPurseBuy()).flatMap(purseBootcoin -> {
            Optional<Transaction> transactionSave = purseBootcoin.getTransactions().stream().filter(
                    transaction -> transaction.getIdTransaction().equalsIgnoreCase(completeEvent.getIdTransaction())).findFirst();
            transactionSave.ifPresent(transaction -> {
                transaction.setState(completeEvent.getState());

            });
            return bootcoinRepository.save(purseBootcoin);
        }).doOnError(e -> log.error("Error processing transaction event for buyer: {}", e.getMessage())).subscribe();

        bootcoinRepository.findById(completeEvent.getIdPurseSeller()).flatMap(purseBootcoin -> {
            Optional<Transaction> transactionSave = purseBootcoin.getTransactions().stream().filter(
                    transaction -> transaction.getIdTransaction().equalsIgnoreCase(completeEvent.getIdTransaction())).findFirst();
            transactionSave.ifPresent(transaction -> {
                transaction.setState(completeEvent.getState());

            });
            return bootcoinRepository.save(purseBootcoin);
        }).doOnError(e -> log.error("Error processing transaction event for seller: {}", e.getMessage())).subscribe();
    }
    private List<Transaction> addPayment(PurseBootcoin purseBootcoin, Transaction transaction) {
        List<Transaction> transactionList = Optional.ofNullable(purseBootcoin.getTransactions())
                .orElseGet(() -> {
                    List<Transaction> newTransaction = new ArrayList<>();
                    return newTransaction;
                });
        transactionList.add(transaction);
        return transactionList;
    }

}
