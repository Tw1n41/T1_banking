package ru.t1bank.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ServiceLogProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${ru.t1bank.kafka.topic.service_logs}")
    private String serviceLogsTopic;

    public void send(String serviceName, Object message, String logType){

        try {
            kafkaTemplate.send(serviceLogsTopic, serviceName, message);
            log.info("Отправлено сообщение {}", serviceLogsTopic);
        } catch (Exception e) {
            log.error("Ошибка при отправке {}", e.getMessage(),e);
            throw e;
        }
    }
}
