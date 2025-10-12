package ru.t1bank.aop;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import ru.t1bank.aop.annotation.LogDatasourceError;
import ru.t1bank.dto.LogDto;
import ru.t1bank.kafka.ServiceLogProducer;
import ru.t1bank.repository.ErrorLogRepository;

import java.time.LocalDateTime;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class ErrorLogAspect {

    private final ServiceLogProducer producer;
    private final ErrorLogRepository errorLogRepository;
    private final ObjectMapper objMapper = new ObjectMapper();

    private final DefaultErrorAttributes errorAttributes;

    @Around("@annotation(LogDatasourceError)")
    public Object logError(ProceedingJoinPoint joinPoint, LogDatasourceError logDatasourceError) throws Throwable {

        try {
            return joinPoint.proceed();
        }
        catch (Exception e) {
            log.error("Ошибка в методе {} : {}", joinPoint.getSignature(),e.getMessage(), e);

            LogDto logDto = LogDto.builder()
                    .timestamp(LocalDateTime.now())
                    .methodSignature(joinPoint.getSignature().toShortString())
                    .exceptionMessage(e.getMessage())
                    .stackTrace(getStackTraceAsString(e))
                    .args(joinPoint.getArgs())
                    .build();

            try {
                producer.send("client-processing",logDto,"ERROR");
            }
            catch (Exception ex) {

                ErrorLog errorLog = ErrorLog.builder()
                        .timestamp(logDto.getTimestamp())
                        .methodSignature(logDto.getMethodSignature())
                        .exceptionMessage(logDto.getExceptionMessage())
                        .stackTrace(logDto.getStackTrace())
                        .args(objMapper.writeValueAsString(logDto.getArgs()))
                        .logLevel("ERROR")
                        .build();

                errorLogRepository.save(errorLog);
            }
            throw e;
        }
    }

    private String getStackTraceAsString(Throwable e) {
        StringBuilder sb = new StringBuilder();
        for (StackTraceElement element : e.getStackTrace()) {
            sb.append(element.toString()).append("\n");
        }
        return sb.toString();
    }
}
