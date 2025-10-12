package ru.t1bank.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import ru.t1bank.Product;

import java.util.UUID;

@Component
@Slf4j
@RequiredArgsConstructor
public class KafkaProductProducer {

    private final KafkaTemplate<String, Product> kafkaTemplate;

    @Value("${ru.t1bank.kafka.topic.product}")
    private String topic;

    public void send(Product product) {
        try {
            kafkaTemplate.send(topic, UUID.randomUUID().toString(), product).get();
        }
        catch (Exception e) {
            log.error("Ошибка отправки продукта: {}", e.getMessage(), e);
        }
        finally {
            kafkaTemplate.flush();
        }
    }

    public void sendProductCreated(Product product) {
        try {
            kafkaTemplate.send(topic, "CREATED", product).get();
        }
        catch (Exception e) {
            log.error("Ошибка при CREATED: {}", e.getMessage(), e);
        }
        finally {
            kafkaTemplate.flush();
        }
    }

    public void sendProductUpdated(Product product) {
        try {
            kafkaTemplate.send(topic, "UPDATED", product).get();
        }
        catch (Exception e) {
            log.error("Ошибка при UPDATED: {}", e.getMessage(), e);
        }
        finally {
            kafkaTemplate.flush();
        }
    }

    public void sendProductDeleted(Product product) {
        try {
            kafkaTemplate.send(topic, "DELETED", product).get();
        }
        catch (Exception e) {
            log.error("Ошибка при DELETED: {}", e.getMessage(), e);
        }
        finally {
            kafkaTemplate.flush();
        }
    }
}
