package ru.t1bank.service.impl;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.t1bank.repository.ProductRegistryRepository;

@Component
@RequiredArgsConstructor
public class CreditProductMetrics {

    private final MeterRegistry meterRegistry;
    private final ProductRegistryRepository productRegistryRepository;

    @PostConstruct
    public void initMetrics() {
        Gauge.builder("credit_products_total", this, value -> productRegistryRepository.count())
                .description("Общее количество кредитных продуктов в системе")
                .register(meterRegistry);
    }
}
