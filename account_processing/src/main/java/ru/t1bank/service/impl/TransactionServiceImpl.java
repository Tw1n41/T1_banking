package ru.t1bank.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.t1bank.Transaction;
import ru.t1bank.aop.annotation.Cached;
import ru.t1bank.repository.TransactionRepository;
import ru.t1bank.service.metrics.TransactionService;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;

    @Override
    @Cached
    public Transaction createTransaction(Transaction transaction) {
        log.info("Сохраняем транзакцию {}", transaction);
        return transactionRepository.save(transaction);
    }

    @Override
    @Cached
    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }
}
