package ru.t1bank.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import ru.t1bank.dto.CardDto;

@Component
@Slf4j
@RequiredArgsConstructor
public class KafkaCardProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${ru.t1bank.kafka.topic.client_cards}")
    private String clientCardsTopic;

    public void send(CardDto dto) {
        try {
            kafkaTemplate.send(clientCardsTopic, dto);
            log.info("CardDto {} отправлено в {}", dto, clientCardsTopic);
        }
        catch (Exception e) {
            log.error("Ошибка отправки {}", e.getMessage(), e);
        }

    }
}
