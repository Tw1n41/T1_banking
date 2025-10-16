package ru.t1bank.service.impl;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.t1bank.repository.AccountRepository;

@Component
@RequiredArgsConstructor
public class AccountMetrics {

    private final MeterRegistry meterRegistry;
    private final AccountRepository accountRepository;

    @PostConstruct
    public void initMetrics() {
        Gauge.builder("accounts_total", this, value -> accountRepository.count())
                .description("Общее количество аккаунтов в системе")
                .register(meterRegistry);
    }
}
