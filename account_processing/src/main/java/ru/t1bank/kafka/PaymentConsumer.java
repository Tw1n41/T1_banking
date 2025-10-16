package ru.t1bank.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;
import ru.t1bank.dto.PaymentDto;
import ru.t1bank.service.metrics.PaymentService;

@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentConsumer {

    private final PaymentService paymentService;

    @KafkaListener(topics = "${ru.t1bank.kafka.topic.client_payments}",
            groupId = "payment-processing")
    public void consume(@Header(KafkaHeaders.RECEIVED_KEY) String messageKey,
                        PaymentDto dto) {
        log.info("Получено сообщение оплаты от {}", dto);
        paymentService.payment(messageKey, dto);
    }
}
