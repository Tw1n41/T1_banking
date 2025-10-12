package ru.t1bank.service.metrics;

import ru.t1bank.dto.TransactionDto;

public interface TransactionProcessingService {

    void process(String messageKey, TransactionDto dto);
}
