package ru.t1bank.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.t1bank.aop.annotation.HttpIncomeRequestLog;
import ru.t1bank.dto.PaymentDto;
import ru.t1bank.service.metrics.PaymentService;

import java.util.UUID;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    @HttpIncomeRequestLog
    public ResponseEntity<PaymentDto> createPayment(@RequestBody PaymentDto dto) {
        String messageKey = UUID.randomUUID().toString();
        PaymentDto result = paymentService.payment(messageKey, dto);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    @HttpIncomeRequestLog
    public ResponseEntity<PaymentDto> getPayment(@PathVariable Long id) {
        return paymentService.getPaymentById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
