package com.nttdata.bootcoin.repository;

import com.nttdata.bootcoin.model.entity.domain.Payment;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface PaymentRepository extends ReactiveMongoRepository<Payment,String> {


}
