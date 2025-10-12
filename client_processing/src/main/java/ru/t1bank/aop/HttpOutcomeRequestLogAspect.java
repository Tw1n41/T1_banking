package ru.t1bank.aop;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class HttpOutcomeRequestLogAspect {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @Value("${spring.ru.t1bank.client_processing}")
    private String serviceName;

    @Value("${ru.t1bank.kafka.topic.service_logs:service_logs}")
    private String topicName;

    @AfterReturning(pointcut = "@annotation(ru.t1bank.annotation.HttpOutcomeRequestLog)", returning = "result")
    public void afterHttpReq(JoinPoint joinPoint, Object res) {

        try {
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();

            Map<String, Object> message = new HashMap<>();
            message.put("timestamp", LocalDateTime.now());
            message.put("methodSignature", signature.toShortString());
            message.put("url", extractUrl(joinPoint.getArgs()));
            message.put("params", joinPoint.getArgs());
            message.put("body", res);

            String jsonMessage = objectMapper.writeValueAsString(message);

            ProducerRecord<String, Object> record = new ProducerRecord<>(topicName, serviceName, jsonMessage);
            record.headers().add("type", "INFO".getBytes());

            kafkaTemplate.send(record);

            log.info("HttpOutcomeRequestLogAspect sent {}", jsonMessage);
        }
        catch (Exception e) {
            log.error("Ошибка HttpOutcomeRequestLogAspect",e);
        }
    }

    private String extractUrl(Object[] args) {
        for (Object arg : args) {
            if (arg instanceof String s && s.startsWith("http")) {
                return s;
            }
        }
        return null;
    }
}
