package ru.t1bank.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.t1bank.Account;
import ru.t1bank.Payment;
import ru.t1bank.dto.PaymentDto;
import ru.t1bank.enums.PaymentStatus;
import ru.t1bank.mapper.PaymentMapper;
import ru.t1bank.repository.AccountRepository;
import ru.t1bank.repository.PaymentRepository;
import ru.t1bank.service.metrics.PaymentService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final AccountRepository accountRepository;
    private final PaymentMapper paymentMapper;

    @Override
    @Transactional
    public PaymentDto payment(String messageKey, PaymentDto dto) {

        log.info("Оплата с {}", dto);

        Account account = accountRepository.findById(dto.getAccountId())
                .orElseThrow(() -> new RuntimeException("Аккаунт " + dto.getAccountId() + " не найден"));

        Payment payment = paymentMapper.toEntity(dto);

        if (payment.getIsCredit() != null && payment.getIsCredit()) {

            BigDecimal outstanding = account.getBalance().subtract(payment.getAmount());

            if (outstanding.compareTo(BigDecimal.ZERO) < 0) {

                log.warn("Недостаточно средств на балансе {}", account.getId());
                payment.setExpired(true);
            }
            else {

                account.setBalance(account.getBalance().subtract(payment.getAmount()));
                accountRepository.save(account);

                payment.setPayedAt(LocalDateTime.now());
                payment.setPaymentStatus(PaymentStatus.CONFIRMED);
            }
        }
        else {

            account.setBalance(account.getBalance().add(payment.getAmount()));
            accountRepository.save(account);
            payment.setPayedAt(LocalDateTime.now());
            payment.setPaymentStatus(PaymentStatus.CONFIRMED);
        }

        payment.setMessageKey(messageKey);
        Payment saved = paymentRepository.save(payment);
        return paymentMapper.toDto(saved);
    }

    @Override
    public Optional<PaymentDto> getPaymentById(Long id) {
        return paymentRepository.findById(id).map(paymentMapper::toDto);
    }
}
