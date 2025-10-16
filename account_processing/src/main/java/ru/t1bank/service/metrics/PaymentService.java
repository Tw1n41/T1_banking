package ru.t1bank.service.metrics;

import ru.t1bank.dto.PaymentDto;

import java.util.Optional;

public interface PaymentService {

    PaymentDto payment(String messageKey,PaymentDto dto);

    Optional<PaymentDto> getPaymentById(Long id);
}
