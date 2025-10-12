package ru.t1bank.mapper;

import org.springframework.stereotype.Component;
import ru.t1bank.Payment;
import ru.t1bank.dto.PaymentDto;

@Component
public class PaymentMapper {

    public PaymentDto toDto(Payment entity) {
        if (entity == null) return null;
        return PaymentDto.builder()
                .id(entity.getId())
                .accountId(entity.getAccountId())
                .paymentDate(entity.getPaymentDate())
                .amount(entity.getAmount())
                .isCredit(entity.getIsCredit())
                .payedAt(entity.getPayedAt())
                .paymentStatus(entity.getPaymentStatus())
                .expired(entity.isExpired())
                .build();
    }

    public Payment toEntity(PaymentDto dto) {
        if (dto == null) return null;
        Payment payment = new Payment();
        payment.setId(dto.getId());
        payment.setAccountId(dto.getAccountId());
        payment.setPaymentDate(dto.getPaymentDate());
        payment.setAmount(dto.getAmount());
        payment.setIsCredit(dto.getIsCredit());
        payment.setPayedAt(dto.getPayedAt());
        payment.setPaymentStatus(dto.getPaymentStatus());
        payment.setExpired(dto.isExpired());
        return payment;
    }
}
