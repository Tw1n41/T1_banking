package ru.t1bank.service.metrics;

import ru.t1bank.Transaction;

import java.util.List;

public interface TransactionService {

    Transaction createTransaction(Transaction transaction);
    List<Transaction> getAllTransactions();
}
