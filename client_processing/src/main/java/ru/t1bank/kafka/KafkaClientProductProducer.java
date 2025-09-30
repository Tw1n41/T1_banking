package ru.t1bank.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import ru.t1bank.dto.ClientProductDto;
import ru.t1bank.enums.ProductKey;

@Component
@Slf4j
@RequiredArgsConstructor
public class KafkaClientProductProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${t1bank.kafka.topic.client_products}")
    private String clientProductsTopic;

    @Value("${t1bank.kafka.topic.client_credit_products}")
    private String clientCreditProductsTopic;

    public void send(ClientProductDto dto) {
        ProductKey key = dto.getProductKey();

        if (key == ProductKey.DC || key == ProductKey.CC || key == ProductKey.NS || key == ProductKey.PENS) {
            kafkaTemplate.send(clientProductsTopic, dto);
            log.info("{} Отправлено в {}", dto, clientProductsTopic);
        } else if (key == ProductKey.IPO || key == ProductKey.PC || key == ProductKey.AC) {
            kafkaTemplate.send(clientCreditProductsTopic, dto);
            log.info("{} Отправлено в {} ", dto, clientCreditProductsTopic);
        } else {
            log.warn("Неизвестный тип продукта: {}", key);
        }
    }
}
