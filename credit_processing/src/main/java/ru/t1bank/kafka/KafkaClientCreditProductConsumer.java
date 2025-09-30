package ru.t1bank.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.t1bank.dto.CreditProductRequestDto;
import ru.t1bank.service.metrics.CreditProductService;


@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaClientCreditProductConsumer {

    private final CreditProductService creditProductService;

    @KafkaListener(topics = "${ru.t1bank.kafka.topic.client_credit_products}", groupId = "credit-processing")
    public void consume(CreditProductRequestDto dto) {
        log.info("Получено сообщение {}", dto);
        creditProductService.processCreditProduct(dto);
    }
}
