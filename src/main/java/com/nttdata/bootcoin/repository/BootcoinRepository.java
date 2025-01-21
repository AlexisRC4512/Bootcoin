package com.nttdata.bootcoin.repository;

import com.nttdata.bootcoin.model.entity.PurseBootcoin;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface BootcoinRepository extends ReactiveMongoRepository<PurseBootcoin,String> {
    Mono<PurseBootcoin> findByNumberPhone(String numberPhone);
    Mono<PurseBootcoin> findByNumberAccount(String numberAccount);
}
