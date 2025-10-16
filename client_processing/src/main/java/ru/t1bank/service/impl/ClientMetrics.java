package ru.t1bank.service.impl;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.t1bank.repository.ClientRepository;

@Component
@RequiredArgsConstructor
public class ClientMetrics {

    private final MeterRegistry meterRegistry;
    private final ClientRepository clientRepository;

    @PostConstruct
    public void initMetrics() {
        Gauge.builder("clients_total", this, value -> clientRepository.count())
                .description("Количество клиентов в системе")
                .register(meterRegistry);
    }
}
