package ru.t1bank.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.t1bank.Transaction;
import ru.t1bank.dto.TransactionDto;
import ru.t1bank.service.metrics.TransactionService;
import ru.t1bank.util.TransactionMapper;

@Component
@Slf4j
@RequiredArgsConstructor
public class ClientTransactionsConsumer {

    private final TransactionService transactionService;
    private final TransactionMapper transactionMapper;

    @KafkaListener(topics = "${t1bank.kafka.topic.client_transactions}", groupId = "account-processing")
    public void consume(TransactionDto dto) {
        log.info("Получено сообщение {}", dto);

        try {
            Transaction transaction = transactionMapper.toEntity(dto);
            transactionService.createTransaction(transaction);
            log.info("Транзакция создана {}", transaction.getId());
        } catch (Exception e) {
            log.error("Ошибка при создании транзакции ", e);
        }
    }
}
