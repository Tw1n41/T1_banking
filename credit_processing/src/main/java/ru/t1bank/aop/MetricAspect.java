package ru.t1bank.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import ru.t1bank.aop.annotation.Metric;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Aspect
@Component
public class MetricAspect {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${metric.time-limit-ms}")
    private long timeLimit;

    public MetricAspect(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Around("@annotation(metric)")
    public Object measureExecutionTime(ProceedingJoinPoint joinPoint, Metric metric) throws Throwable {

        long startTime = System.currentTimeMillis();
        Object result = null;
        Throwable thrown = null;

        try {
            result = joinPoint.proceed();
            return result;
        } catch (Throwable e) {
            thrown = e;
            throw e;
        } finally {
            long duration = System.currentTimeMillis() - startTime;

            if (duration > timeLimit) {
                String methodSignature = joinPoint.getSignature().toShortString();
                Object[] args = joinPoint.getArgs();

                Map<String, Object> logMessage = new HashMap<>();
                logMessage.put("type", "WARNING");
                logMessage.put("method", methodSignature);
                logMessage.put("executionTimeMs", duration);
                logMessage.put("args", Arrays.toString(args));

                log.warn("Метод {} превысил лимит времени ({} ms > {} ms)",
                        methodSignature, duration, timeLimit);

                kafkaTemplate.send("service_logs", logMessage);
            }
        }
    }
}
