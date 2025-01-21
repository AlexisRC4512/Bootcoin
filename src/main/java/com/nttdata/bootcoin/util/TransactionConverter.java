package com.nttdata.bootcoin.util;

import com.nttdata.bootcoin.model.entity.domain.Transaction;
import com.nttdata.bootcoin.model.entity.events.TransactionEvents;

public class TransactionConverter {
    public static Transaction toTransation(TransactionEvents transactionEvents) {
        Transaction transaction = new Transaction();
        transaction.setDate(transactionEvents.getDate());
        transaction.setIdTransaction(transactionEvents.getIdTransaction());
        transaction.setAmount(transactionEvents.getAmount());
        transaction.setState(transactionEvents.getState());
        return transaction;
    }
}
