package ru.t1bank.aop;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class HttpIncomeRequestLogAspect {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${spring.ru.t1bank.account_processing}")
    private String serviceName;

    @Value("${ru.t1bank.kafka.topic.service_logs:service_logs}")
    private String serviceLogsTopic;

    @Before("@annotation(ru.t1bank.aspects.annotations.HttpIncomeRequestLog)")
    public void logHttpIncomeReq(JoinPoint joinPoint) {

        try {
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            String methodName = signature.toShortString();

            Map<String, Object> payload = new HashMap<>();
            payload.put("timestamp", Instant.now().toString());
            payload.put("methodSignature", methodName);

            Object[] args = joinPoint.getArgs();
            payload.put("args", args != null ? args : "no args");

            String jsonPayload = objectMapper.writeValueAsString(payload);

            ProducerRecord<String, Object> record = new ProducerRecord<>(serviceLogsTopic, serviceName, jsonPayload);
            record.headers().add("type", "INFO".getBytes());

            kafkaTemplate.send(record);
            log.info("HTTP request from {}: {}", serviceName, jsonPayload);

        } catch (Exception e) {
            log.error("Ошибка отправки HTTP в Kafka {}", e.getMessage(), e);
        }
        }
    }

