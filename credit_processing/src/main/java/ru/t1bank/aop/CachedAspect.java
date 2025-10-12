package ru.t1bank.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Aspect
@Component
@Slf4j
public class CachedAspect {

    @Value("${cache.ttl-seconds:60}")
    private long ttlSeconds;

    private final Map<Object, CacheEntry> cache = new ConcurrentHashMap<>();

    @Around("@annotation(com.example.aspect.Cached)")
    public Object cacheMethod(ProceedingJoinPoint joinPoint) throws Throwable {

        String methodName = joinPoint.getSignature().toShortString();
        Object[] args = joinPoint.getArgs();

        Object key = (args.length == 1) ? args[0] : args.hashCode();

        CacheEntry entry = cache.get(key);
        long now = Instant.now().getEpochSecond();

        if (entry != null && now - entry.timestamp < ttlSeconds) {
            log.info("Возвращаем данные из кэша для {}", methodName);
            return entry.value;
        }

        Object result = joinPoint.proceed();

        cache.put(key, new CacheEntry(result, now));
        log.info("Добавлена запись в кэш: {} -> {}", key, result);

        return result;
    }

    private record CacheEntry(Object value, long timestamp) {}
}
