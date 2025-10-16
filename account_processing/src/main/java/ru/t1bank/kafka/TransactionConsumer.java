package ru.t1bank.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;
import ru.t1bank.dto.TransactionDto;
import ru.t1bank.service.metrics.TransactionProcessingService;

@Component
@RequiredArgsConstructor
@Slf4j
public class TransactionConsumer {

    private final TransactionProcessingService transactionProcessingService;

    @KafkaListener(topics = "${ru.t1bank.kafka.topic.client_transactions}",
            groupId = "account-processing")
    public void consume(@Header(KafkaHeaders.RECEIVED_KEY) String messageKey,
                        TransactionDto dto) {

        try {

            log.info("Получено сообщение от {}", dto);
            transactionProcessingService.process(messageKey,dto);
        }
        catch (Exception e) {
            log.error("Ошибка при транзакции сообщения {}",e.getMessage(), e);
        }
    }
}
