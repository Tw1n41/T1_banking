package ru.t1bank.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;
import org.springframework.transaction.aspectj.AnnotationTransactionAspect;
import org.springframework.transaction.interceptor.BeanFactoryTransactionAttributeSourceAdvisor;
import ru.t1bank.Account;
import ru.t1bank.Payment;
import ru.t1bank.Transaction;
import ru.t1bank.dto.TransactionDto;
import ru.t1bank.repository.AccountRepository;
import ru.t1bank.repository.PaymentRepository;
import ru.t1bank.repository.TransactionRepository;
import ru.t1bank.service.metrics.TransactionProcessingService;
import ru.t1bank.service.metrics.TransactionService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionProcessingServiceImpl implements TransactionProcessingService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final PaymentRepository paymentRepository;

    private final BeanFactoryTransactionAttributeSourceAdvisor transactionAdvisor;

    private final AnnotationTransactionAspect transactionAspect;

    private final TransactionService transactionService;

    @Value("${fraud.transaction.limit.count:10}")
    private int limitN;

    @Value("${fraud.transaction.limit.period.minutes:1}")
    private long limitPeriodMinutes;

    @Override
    @Transactional
    public void process(String messageKey, TransactionDto dto) {

        log.info("Process with key {} dto {} started", messageKey, dto);

        Transaction transaction = Transaction.builder()
                .messageKey(messageKey)
                .transactionId(dto.getTransactionId())
                .accountId(dto.getAccountId())
                .cardId(dto.getCardId())
                .type(dto.getType())
                .amount(dto.getAmount())
                .transactionDate(dto.getTransactionDate())
                .build();

        transactionRepository.save(transaction);

        Account account = accountRepository.findById(dto.getAccountId())
                .orElseThrow(() -> new RuntimeException("Account not found " + dto.getAccountId()));

        if (account.isBlocked()) {
            log.warn("Account {} blocked", account.getId());

            transaction.setFrozen(true);
            transactionRepository.save(transaction);
            return;
        }

        if (dto.getCardId() != null) {

            LocalDateTime to = LocalDateTime.now();
            LocalDateTime from = to.minusMinutes(limitPeriodMinutes);
            long cnt = transactionRepository.countByCardIdBetween(dto.getCardId().toString(), from, to);

            if (cnt > limitN) {
                log.warn("Card {} exceeded limit {}, blocking account {}", dto.getCardId(), cnt, account.getId());

                transaction.setFrozen(true);
                transactionRepository.save(transaction);

                account.setBlocked(true);
                accountRepository.save(account);
                return;
            }
        }
        
        if ("DEBIT".equalsIgnoreCase(dto.getType())){
            applyDebit(account, dto.getAmount(), transaction);
        } else if ("CREDIT".equalsIgnoreCase(dto.getType())) {
            applyCredit(account, dto, transaction);
        }
        else log.warn("Unknown transaction type {}", dto.getType());
    }

    private void applyDebit(Account account, BigDecimal amount, Transaction transaction) {

        if (account.getBalance().compareTo(amount) >= 0) {

            account.setBalance(account.getBalance().subtract(amount));
            accountRepository.save(account);

            transaction.setTransactionDate(LocalDateTime.now());
            transactionRepository.save(transaction);

            log.info("Debited {} from {}", amount, account.getId());
        }
        else {
            log.warn("Not enough balance on account {}", account);
            transaction.setFrozen(true);
            transactionRepository.save(transaction);
        }
    }

    private void applyCredit(Account account, TransactionDto dto, Transaction transaction) {

        BigDecimal amount = dto.getAmount();

        account.setBalance(account.getBalance().add(amount));
        accountRepository.save(account);

        transaction.setTransactionDate(LocalDateTime.now());
        transactionRepository.save(transaction);

        log.info("Начислено {} на счет {}", amount, account.getId());

        if (account.getIsRecalc()) {
            int months = dto.getMonthCount();
            BigDecimal interestRate = dto.getInterestRate();

            if (months > 0 && interestRate != null) {
                createPaymentShedule(account.getId(), dto.getAmount(), interestRate, months);
            }

            List<Payment> payments = paymentRepository.findByAccountId(account.getId());

            for (Payment payment : payments) {
                if (payment.getPaymentDate().isEqual(LocalDate.now()) && payment.getPayedAt() == null) {

                    if (account.getBalance().compareTo(payment.getAmount()) >= 0) {
                        account.setBalance(account.getBalance().subtract(payment.getAmount()));
                        payment.setPayedAt(LocalDateTime.now());
                        paymentRepository.save(payment);
                        accountRepository.save(account);
                        log.info("Списано {} со счёта {}", payment.getAmount(), account.getId());
                    } else {
                        payment.setExpired(true);
                        paymentRepository.save(payment);
                        log.warn("Недостаточно средств, платёж просрочен для счёта {}", account.getId());
                    }
                }
            }
        }
    }

    private void createPaymentShedule(Long accountId, BigDecimal amount, BigDecimal rate, int months) {

        BigDecimal monthlyRate = rate.divide(BigDecimal.valueOf(12 * 100), 10, RoundingMode.HALF_UP);
        BigDecimal onePlusRatePowN = BigDecimal.ONE.add(monthlyRate).pow(months);
        BigDecimal numerator = monthlyRate.multiply(onePlusRatePowN);
        BigDecimal denominator = onePlusRatePowN.subtract(BigDecimal.ONE);
        BigDecimal monthlyPayment = amount.multiply(numerator.divide(denominator, 10, RoundingMode.HALF_UP))
                .setScale(2, RoundingMode.HALF_UP);

        LocalDateTime paymentDate = LocalDate.now().plusMonths(1).atStartOfDay();

        for (int i = 1; i <= months; i++) {
            Payment payment = new Payment();
            payment.setAccountId(accountId);
            payment.setPaymentDate(LocalDate.from(paymentDate));
            payment.setAmount(monthlyPayment);
            payment.setIsCredit(true);
            payment.setExpired(false);
            paymentRepository.save(payment);
            paymentDate = paymentDate.plusMonths(1);
        }

        log.info("Создан график из {} платежей для счёта {}", months, accountId);
    }

}
